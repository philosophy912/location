<template>
  <div id="map"></div>
  <div id="myChart">
    <div id="top" ></div>
    <div id="bottom"></div>
  </div>
</template>

<script setup lang="js">
import maps from './map.js'
import {fetchData} from "./api";
import {ref, onMounted} from "vue";
import * as echarts from 'echarts'

const chart = ref()


let BM;
let map;

maps.then(() => {
  BM = window.BM
  // 宜宾翠屏山 28.770281, 104.621379
  // 宜宾市大观楼地址104.62922,28.77064
  let latlng = BM.latLng(28.77064, 104.62922);
  BM.Config.HTTP_URL = 'http://localhost:9000';
  map = BM.map('map', 'bigemap.baidu-map', {crs: BM.CRS.Baidu, center: latlng, zoom: 14, zoomControl: true});
  // 后台获取数据
  fetchData().then(res => {
    // console.log(res)
    res.forEach((item) => {
      // console.log(JSON.stringify(item))
      const id = item["id"]
      const title = item["name"]
      const lng = item["lng"]
      const lat = item["lat"]
      const address = item["address"]
      // console.log("lng is " + lng + " lat is " + lat)
      // 前台利用数据展示  {title: address, zIndexOffset: 1000}
      let marker = BM.marker([lat, lng])
          .bindTooltip(title)
          .bindPopup('<p>纳税人识别号：</br>生产经营地址：</br>累计销售收入：</br>')
          .addTo(map);
      marker.openPopup()
    })
  })
})

onMounted(() => {
  echartsInit()
  echartsInit1()
})

const echartsInit = () => {
  const myChart = echarts.init(document.getElementById("top"))
  var option = {
    title: {
      text: 'ECharts 入门示例'
    },
    tooltip: {},
    legend: {
      data: ['销量']
    },
    xAxis: {
      data: ['衬衫', '羊毛衫', '雪纺衫', '裤子', '高跟鞋', '袜子']
    },
    yAxis: {},
    series: [
      {
        name: '销量',
        type: 'bar',
        data: [5, 20, 36, 10, 10, 20]
      }
    ]
  };
  myChart.setOption(option);
}

const echartsInit1 = () => {
  const myChart = echarts.init(document.getElementById("bottom"))
  var option = {
    title: {
      text: 'ECharts 入门示例'
    },
    tooltip: {},
    legend: {
      data: ['销量']
    },
    xAxis: {
      data: ['衬衫', '羊毛衫', '雪纺衫', '裤子', '高跟鞋', '袜子']
    },
    yAxis: {},
    series: [
      {
        name: '销量',
        type: 'bar',
        data: [5, 20, 36, 10, 10, 20]
      }
    ]
  };
  myChart.setOption(option);
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
  width: 300px;
  background-color: black;
  opacity: 0.5;
  margin-top: 70px;
}

#top {
  position: absolute;
  z-index: 10;
  height: 500px;
  left: 1px;
  width: 300px;
  color: white;
}

#bottom {
  position: absolute;
  z-index: 10;
  height: 500px;
  width: 300px;
  top: 600px;
  left: 1px;
  color: white;
}

.tooltip {
  color: white;
  background: transparent;
  border: none;
  font-size: 12px;
}

.tooltip::before {
  display: none;
}
</style>
