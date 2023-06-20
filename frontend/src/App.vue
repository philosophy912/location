<template>
  <div id="map"></div>
  <div id="myChart">
    <div id="user">
      <el-select v-model="value" class="m-2" placeholder="请选择区域" size="large" @change="changeSelectValue"
                 clearable>
        <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-input v-model="input" placeholder="请输入要查询的企业名字" clearable
                @change="queryCompany"/>
    </div>
    <div id="top"></div>
    <div id="bottom"></div>
  </div>
</template>

<script setup lang="js">
import maps from './map.js'
import {
  fetchArea,
  fetchChart,
  fetchChartByArea,
  fetchChartByIds,
  fetchChartByMarkerIds,
  fetchData,
  fetchIndustryPark
} from "./api";
import {onMounted, ref} from "vue";
import * as echarts from 'echarts'

let BM;
let map;
let topChart;
let bottomChart;

// 添加marker集合
let markers = [];

const input = ref('')
const value = ref('');
const options = ref([])


// 测试用的
// let testMarker;

// 这里从后端获取数据
const getIndustryPark = () => {
  fetchIndustryPark().then(res => {
    const data = res.data;
    data.forEach(obj => {
      const selectData = {
        "value": obj,
        "label": obj
      }
      options.value.push(selectData);
    })
  })
}

const changeSelectValue = () => {
  const selectValue = value.value
  console.log(selectValue)
  if (selectValue !== '') {
    const request = {"name": selectValue}
    fetchChartByArea(request).then(res => {
      const res_data = res.data
      const sales = res_data.taxes
      const taxes = res_data.sales
      const option1 = generatorOption(sales.title, sales.xAxis, sales.yAxis)
      const option2 = generatorOption(taxes.title, taxes.xAxis, taxes.yAxis)
      topChart.setOption(option1);
      bottomChart.setOption(option2);
    })
  }
}

const clearMarkers = () => {
  markers.forEach((marker) => {
    marker.remove();
  })
  // 清空marker数组
  markers = [];
}

const queryCompany = () => {
  // 获取输入框内容
  const name = input.value
  setMarker(name)
}

const setMarker = (name) => {
  // 这里先清空所有的marker，再获取数据
  clearMarkers()
  const data = {"name": name}
  // 后台获取数据
  fetchData(data).then(res => {
    // console.log(res)
    const responseMarkers = res.data
    // 遍历数据
    responseMarkers.forEach((item) => {
      const id = item["id"];
      // 社会信用代码
      const socialCreditCode = item["socialCreditCode"];
      // 纳税人名称
      const taxPersonName = item["taxPersonName"];
      // 主管局
      const supervisionUnit = item["supervisionUnit"];
      // 行业名称
      const industryName = item["industryName"];
      // 经度信息
      const longitude = item["longitude"];
      // 纬度信息
      const latitude = item["latitude"];
      // 前台利用数据展示  {title: address, zIndexOffset: 1000}
      const marker = BM.marker([latitude, longitude])
          .bindTooltip(taxPersonName)
          // 纳税人名称、行业、主管局
          .bindPopup('<p><b>纳税人名称：</b>' + taxPersonName + '</br><b>行业</b>：' + industryName + '</br><b>主管局</b>：' + supervisionUnit + '</br>')
          .addTo(map);
      marker.id = id
      markers.push(marker)
      // 点击之后的操作
      marker.addEventListener("click", () => {
        // marker的ID，可以通过这个来查数据
        console.log(marker.id)
        let id = marker.id
        updateChart(id)
      })
    })
  })
}

const drawPloyGons = () => {
  fetchArea().then(res => {
    const data = res.data
    // console.log(data)
    data.forEach(area => {
      // console.log(area.name)
      drawPloyGon(area)
    })
  })
}

const drawPloyGon = (area) => {
  let latlngs = area.location
  //创建多边形，并设置填充颜色 ，具体详细API请参见：http://www.bigemap.com/offlinemaps/api/#polygon
  let polygon = BM.polygon(latlngs, {weight: 1, color: '#369'}).addTo(map)
  polygon.bindPopup(area.name)
  // polygon.openPopup()
  polygon.on("click", (e) => {
    // 能够获取到经纬度
    // console.log(e.latlng)
    const ids = getPolygonMarkers(latlngs)
    const requestData = {"ids": ids}
    fetchChartByMarkerIds(requestData).then(res => {
      const res_data = res.data
      const sales = res_data.taxes
      const taxes = res_data.sales
      const option1 = generatorOption(sales.title, sales.xAxis, sales.yAxis)
      const option2 = generatorOption(taxes.title, taxes.xAxis, taxes.yAxis)
      topChart.setOption(option1);
      bottomChart.setOption(option2);
    })
  })
}

const getPolygonMarkers = (latlngs) => {
  let polygonMarkers = []
  markers.forEach(v => {
    let latlng = v.getLatLng()
    //点的平面坐标
    let point = [latlng.lat, latlng.lng]
    // let point = map.project([latlng.lat, latlng.lng]);
    if (pointInPolygon(point, latlngs)) {
      polygonMarkers.push(v.id)
    }
  })
  // 获取到了这个矩形框的点
  // console.log(polygonMarkers)
  return polygonMarkers;
}


const pointInPolygon = (point, polygon) => {
  let x = point[0], y = point[1];
  let inside = false;

  for (let i = 0, j = polygon.length - 1; i < polygon.length; j = i++) {
    let xi = polygon[i][0], yi = polygon[i][1];
    let xj = polygon[j][0], yj = polygon[j][1];

    let intersect = ((yi > y) !== (yj > y)) &&
        (x < (xj - xi) * (y - yi) / (yj - yi) + xi);
    if (intersect) inside = !inside;
  }
  return inside;
}


const initMap = () => {
  maps.then(() => {
    BM = window.BM
    // 宜宾翠屏山 28.770281, 104.621379
    // 宜宾市大观楼地址104.62922,28.77064
    let latlng = BM.latLng(28.77064, 104.62922);
    BM.Config.HTTP_URL = 'http://localhost:9000';
    map = BM.map('map', 'bigemap.baidu-map', {crs: BM.CRS.Baidu, center: latlng, zoom: 14, zoomControl: true});
    setMarker()
    drawPloyGons()
    let drawnItems = new BM.FeatureGroup();
    //添加在地图上
    map.addLayer(drawnItems);
    // 为多边形设置一个标题
    BM.drawLocal.draw.toolbar.buttons.polygon = '添加一个多边形';
    //实例化鼠标绘制的控件
    let drawControl = new BM.Control.Draw({
      position: 'topright',//位置
      //控制绘制的图形
      // TODO 如何去掉圆形不清楚
      draw: {
        polyline: false,
        polygon: false,
        rectangle: true,
        circle: false,
        marker: false
      },
      edit: {featureGroup: drawnItems}
    });
    map.addControl(drawControl);
    //监听绘画完成事件
    map.on(BM.Draw.Event.CREATED, (e) => {
      let type = e.layerType, layer = e.layer;
      if (type === 'rectangle') {
        let position = layer.getBounds()
        // 这里获取到了左上角和右下角的坐标点
        let left_top = position.getNorthWest()
        let right_bottom = position.getSouthEast()
        // 过滤在框内的坐标点
        updateChartByIds(right_bottom.lat, left_top.lng, left_top.lat, right_bottom.lng)
      } else {
        console.log("不支持画圆，后续要去掉")
      }
      drawnItems.addLayer(layer);
    });
  })
}


onMounted(() => {
  initMap();
  topChart = echarts.init(document.getElementById("top"))
  bottomChart = echarts.init(document.getElementById("bottom"))
  getIndustryPark();
})

// 生成option用于图片显示
const generatorOption = (title, xAxis, yAxis) => {
  return {
    title: {
      text: title
    },
    xAxis: {
      data: xAxis
    },
    yAxis: {},
    series: [
      {
        type: 'bar',
        data: yAxis,
        itemStyle: {
          boarderRadius: 5,
          borderWidth: 1,
          borderType: 'solid',
          borderColor: '#73c0de',
          shadowColor: '#5470c6',
          shadowBlur: 3
        },
        label: {
          show: true,
          position: 'top'
        }
      }
    ]
  }
}


const updateChartByIds = (x1, y1, x2, y2) => {
  const data = {"x1": x1, "y1": y1, "x2": x2, "y2": y2}
  console.log(JSON.stringify(data))
  fetchChartByIds(data).then(res => {
    const res_data = res.data
    const sales = res_data.taxes
    const taxes = res_data.sales
    const option1 = generatorOption(sales.title, sales.xAxis, sales.yAxis)
    const option2 = generatorOption(taxes.title, taxes.xAxis, taxes.yAxis)
    topChart.setOption(option1);
    bottomChart.setOption(option2);
  })
}


const updateChart = (id) => {
  fetchChart(id).then(res => {
    const data = res.data
    const sales = data.taxes
    const taxes = data.sales
    const option1 = generatorOption(sales.title, sales.xAxis, sales.yAxis)
    const option2 = generatorOption(taxes.title, taxes.xAxis, taxes.yAxis)
    topChart.setOption(option1);
    bottomChart.setOption(option2);
  })
}


</script>

<style>

#map {
  position: absolute;
  top: 0;
  bottom: 0;
  width: 100%;
//height: 100vh;
}

#myChart {
  position: absolute;
  z-index: 10;
  height: 100%;
  width: 500px;
  background-color: black;
  opacity: 0.5;
//margin-top: 60px;
}

#user {
  position: absolute;
  z-index: 10;
  height: 80px;
  width: 450px;
  margin-left: 30px;
  display: flex;
  align-items: center;
}

.el-input {
  margin-left: 5px;
}

.el-select {
  margin-right: 10px;
  margin-left: 10px;
}

#top {
  position: absolute;
  z-index: 10;
  height: 450px;
  left: 10px;
  top: 100px;
  width: 450px;
//color: white;
}

#bottom {
  position: absolute;
  z-index: 10;
  height: 450px;
  width: 500px;
  top: 500px;
  left: 1px;
//color: white;
}

.tooltip {
  color: white;
  background: transparent;
  border: none;
  font-size: 10px;
}

.tooltip::before {
  display: none;
}
</style>
