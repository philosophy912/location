<template>
  <div id="map"></div>
  <div id="myChart">
    <div id="top"></div>
    <div id="bottom"></div>
  </div>
</template>

<script setup lang="js">
import maps from './map.js'
import {fetchChart, fetchData} from "./api";
import {onMounted} from "vue";
import * as echarts from 'echarts'

let BM;
let map;
let topChart;
let bottomChart;

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
      let marker = BM.marker([latitude, longitude])
          .bindTooltip(taxPersonName)
          // 纳税人名称、行业、主管局
          .bindPopup('<p><b>纳税人名称：</b>' + taxPersonName + '</br><b>行业</b>：' + industryName + '</br><b>主管局</b>：' + supervisionUnit + '</br>')
          .addTo(map);
      marker.openPopup()
      marker.id = id
      // 点击之后的操作
      marker.addEventListener("click", () => {
        // marker的ID，可以通过这个来查数据
        console.log(marker.id)
        let id = marker.id
        updateChart(id)
        // echartsInit()
        // echartsInit1()
      })
    })
  })
  let drawnItems = new BM.FeatureGroup();
  //添加在地图上
  map.addLayer(drawnItems);
  // 为多边形设置一个标题
  BM.drawLocal.draw.toolbar.buttons.polygon = '添加一个多边形';
  //实例化鼠标绘制的控件
  var drawControl = new BM.Control.Draw({
    position: 'topright',//位置
    //控制绘制的图形
    draw: {
      polyline: {
        //单独设置线的颜色为红色，其它为默认颜色
        shapeOptions:{
          color:'red'
        }
      },
      polygon: true,
      circle: true,
      marker: true
    },
    edit: { featureGroup: drawnItems }
  });

  map.addControl(drawControl);
  //监听绘画完成事件
  map.on(BM.Draw.Event.CREATED, (e) => {
    let type = e.layerType, layer = e.layer;
    if (type === 'marker') {
      //如果是标注，实现一个点击出现的提示
      layer.bindPopup('A popup!');
    }
    drawnItems.addLayer(layer);
  });

})


onMounted(() => {
  topChart = echarts.init(document.getElementById("top"))
  bottomChart = echarts.init(document.getElementById("bottom"))
})


const updateChart = (id) => {
  fetchChart(id).then(res => {
    const data = res.data
    // console.log(data)
    const sales = data.taxes
    const taxes = data.sales
    // console.log(sales.yAxis)
    // console.log(typeof (sales.yAxis))
    // console.log(taxes)
    const option1 = {
      title: {
        text: sales.title
      },
      xAxis: {
        data: sales.xAxis
      },
      yAxis: {},
      series: [
        {
          type: 'bar',
          data: sales.yAxis,
          itemStyle: {
            boarderRadius: 5,
            borderWidth: 1,
            borderType: 'solid',
            borderColor: '#73c0de',
            shadowColor: '#5470c6',
            shadowBlur: 3
          }
        }
      ]
    }
    const option2 = {
      title: {
        text: taxes.title
      },
      xAxis: {
        data: taxes.xAxis
      },
      yAxis: {},
      series: [
        {
          type: 'bar',
          data: taxes.yAxis,
          itemStyle: {
            boarderRadius: 5,
            borderWidth: 1,
            borderType: 'solid',
            borderColor: '#73c0de',
            shadowColor: '#5470c6',
            shadowBlur: 3
          }
        }
      ]
    }
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
  margin-top: 70px;
}

#top {
  position: absolute;
  z-index: 10;
  height: 500px;
  left: 1px;
  width: 500px;
//color: white;
}

#bottom {
  position: absolute;
  z-index: 10;
  height: 500px;
  width: 500px;
  top: 600px;
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
