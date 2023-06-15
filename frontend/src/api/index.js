import request from '../utils/request';

// export const fetchData = () => {
//     return request({
//         url: './locations.json',
//         method: 'get'
//     });
// };

// 这里获取marker点, 其中data的数据不传则返回所有的数据，也就是data为空， 如果是data中的name不为空，则模糊查询包含name字符的marker点，
// 查询的名字是taxPersonName这个字段的名字 表的字段名字是TAX_PERSON_NAME
export const fetchData = (data) => {
    return request({
        url: '/tax/markers',
        method: 'post',
        data
    });
}
// 这里获取整个工业园区的非重复数据，就是你有多少个工业园区，用来做下拉菜单用的，不需要传递任何参数
export const fetchIndustryPark = () => {
    return request({
        url: '/tax/industryPark',
        method: 'get'
    })
}
// 这里是获取某个mark点的图表数据， get方法，传入的参数是marker的id，对应就是数据库中表T_COMPANY_INFO的ID
export const fetchChart = (id) => {
    return request({
        url: '/tax/chart?id=' + id,
        method: 'get'
    });
}
// 这里是获取画框的marker点， 统计所有的点的税收数据，其中data传递的是x1, x2, y1, y2数据，就是矩形框的左上角和右下角的经纬度信息
export const fetchChartByIds = (data) => {
    return request({
        url: '/tax/ids',
        method: 'post',
        data
    });
}
// 这里获的是某个工业园区的所有的税收信息， 这个地方data需要传递name， 且这个name必须是完整的工业园区数据，主要是下拉菜单选中的数据传进来就可以了。
export const fetchChartByArea = (data) => {
    return request({
        url: '/tax/area',
        method: 'post',
        data
    });
}