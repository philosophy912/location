# -*- coding:utf-8 -*-
# --------------------------------------------------------
# Copyright (C), 2016-2021, lizhe, All rights reserved
# --------------------------------------------------------
# @Name:        data_import.py
# @Author:      lizhe
# @Created:     2023/6/23 - 11:55
# --------------------------------------------------------
import os
import json
from typing import List, Dict

suffix = "-i https://pypi.douban.com/simple"
prefix = "pip install"

try:
    import xlwings as xw
except ModuleNotFoundError:
    os.system(f"{prefix} xlwings {suffix}")
finally:
    import xlwings as xw
    from xlwings import Sheet

try:
    from loguru import logger
except ModuleNotFoundError:
    os.system(f"{prefix} loguru {suffix}")
finally:
    from loguru import logger

try:
    import requests
except ModuleNotFoundError:
    os.system(f"{prefix} requests {suffix}")
finally:
    import requests

import_file = "临港局单位纳税人.xls"
server_ip = "127.0.0.1"
server_port = "8080"


def read_excel(excel_file: str):
    logger.info(f"开始读取文件【{excel_file}】")
    app = xw.App(visible=False, add_book=False)
    app.display_alerts = False
    app.screen_updating = False
    # 打开workbook
    workbook = app.books.open(excel_file)
    # 获取第一个sheet
    sheet = workbook.sheets[0]
    # 获取最大的column的值
    column_length = sheet.used_range.last_cell.column
    logger.trace(f"column length is {column_length}")
    # 获取最大的row的值
    row_length = sheet.used_range.last_cell.row
    logger.trace(f"row length is {row_length}")
    # 获取第一行的值
    title_dict = get_title_dict(sheet, column_length)
    # 重新组合数据
    # {'id': 'A', 'socialcreditcode': 'B', 'taxpersonname': 'C', 'supervisionunit': 'D', 'industryname': 'E',
    # 'longitude': 'F', 'latitude': 'G', 'country': 'H', 'township': 'I', 'industrypark': 'J',
    # 2021: {'销售收入': 'K', '税收收入': 'N'}, 2022: {'销售收入': 'L', '税收收入': 'O'},
    # 2023: {'销售收入': 'M', '税收收入': 'P'}}
    logger.trace(title_dict)
    handle_row_value(sheet, title_dict, row_length)
    workbook.close()
    logger.info("已完成数据的插入")


def request_data(company_info: Dict, url: str = "/tax/insert"):
    json_data = json.dumps(company_info, ensure_ascii=False)
    logger.trace(f"data is {json_data}")
    headers = {"Content-Type": "application/json;charset=UTF-8",
               "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36 Edg/114.0.1823.58"}
    url = f"http://{server_ip}:{server_port}{url}"
    logger.trace(f"url is {url}")
    response = requests.post(url, data=json_data.encode("utf-8"), headers=headers)
    resp = response.json()
    if resp["code"] != 20000:
        error_code = resp["errorInfo"]
        raise RuntimeError(f"错误信息{error_code}")


def handle_row_value(sheet: Sheet, title_dict: Dict, row_length: int):
    for i in range(2, row_length + 1):
        logger.info(f"开始处理第{i - 1}行数据")
        company_info = dict()
        company_info["socialCreditCode"] = sheet.range(f"{title_dict['socialcreditcode']}{i}").value
        company_info["taxPersonName"] = sheet.range(f"{title_dict['taxpersonname']}{i}").value
        company_info["supervisionUnit"] = sheet.range(f"{title_dict['supervisionunit']}{i}").value
        company_info["industryName"] = sheet.range(f"{title_dict['industryname']}{i}").value
        company_info["longitude"] = sheet.range(f"{title_dict['longitude']}{i}").value
        company_info["latitude"] = sheet.range(f"{title_dict['latitude']}{i}").value
        company_info["county"] = sheet.range(f"{title_dict['country']}{i}").value
        company_info["township"] = sheet.range(f"{title_dict['township']}{i}").value
        company_info["industryPark"] = sheet.range(f"{title_dict['industrypark']}{i}").value
        # 税收情况
        tax = title_dict["tax"]
        tax_infos = []
        for year, content in tax.items():
            tax_info = dict()
            tax_info["year"] = year
            tax_revenue = sheet.range(f"{tax[year]['税收收入']}{i}").value
            sales_revenue = sheet.range(f"{tax[year]['销售收入']}{i}").value
            tax_info["taxRevenue"] = tax_revenue
            tax_info["salesRevenue"] = sales_revenue
            tax_infos.append(tax_info)
        company_info["taxInfos"] = tax_infos
        # 上传请求数据
        request_data(company_info)


def get_title_dict(sheet: Sheet, max_column: int):
    title_dict = {}
    years = []
    for i in range(1, max_column + 1):
        column_letter = get_column_letter(i)
        cell_value = sheet.range(f"{column_letter}1").value
        # logger.info(cell_value)
        if len(cell_value) < 5:
            title_dict[cell_value] = column_letter
        else:
            try:
                # 预处理年份的内容
                int(cell_value[:4])
                years.append((cell_value, column_letter))
            except ValueError:
                title_dict[cell_value] = column_letter
    # print(years)
    year_dict = handle_years(years)
    title_dict["tax"] = year_dict
    return title_dict


def handle_years(years: List) -> Dict:
    # ['2021年销售收入', '2022年销售收入', '2023年销售收入', '2021年税收入库', '2022年税收入库', '2023年税收入库']
    year_set = set(map(lambda x: int(x[0][:4]), years))
    year_dict = {x: {'销售收入': '', "税收收入": ''} for x in year_set}
    for line in years:
        year_line, column_letter = line
        year = int(year_line[:4])
        if "销售" in year_line:
            year_dict[year]['销售收入'] = column_letter
        if "税收" in year_line:
            year_dict[year]["税收收入"] = column_letter
    # print(year_dict)
    return year_dict


def get_column_letter(column_index: int) -> str:
    # 根据ascii码来计算列名
    start_value = 64
    max_char = 26
    mods = []
    div, mod = column_index // max_char, column_index % max_char
    # print(div, mod)
    if mod == 0:
        mods.insert(0, chr(max_char + start_value))
        div = div - 1
    else:
        mods.insert(0, chr(mod + start_value))
    while div != 0:
        div, mod = div // max_char, div % max_char
        if mod == 0:
            mods.insert(0, chr(max_char + start_value))
            div = div - 1
        else:
            mods.insert(0, chr(mod + start_value))
    return "".join(mods)


read_excel(import_file)
