<template>
  <div id="map"></div>
</template>

<script setup lang="js">
import maps from './map.js'
import {ref} from "vue";
import {fetchData} from "./api/index";

let BM;
let map;
let markers = ref < [] > ([]);
maps.then(() => {
  BM = window.BM
  // 宜宾市大观楼地址104.62922,28.77064
  let latlng = BM.latLng(28.77064, 104.62922);
  BM.Config.HTTP_URL = 'http://localhost:9000';
  map = BM.map('map', 'bigemap.baidu-map', {crs: BM.CRS.Baidu, center: latlng, zoom: 14, zoomControl: true});
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
      BM.marker([lat, lng], {title: address, zIndexOffset: 1000}).addTo(map).bindTooltip(title).openTooltip();
    })
  })
  // let marker = BM.marker([30.708194, 104.097419], {title: "我日你大爷的", zIndexOffset: 1000}).addTo(map);
  // marker.bindTooltip("你大爷的，怎么看不到呢").openTooltip();
  // // 104.104301,30.714433
  // let marker1 = BM.marker([30.714433, 104.104301], {title: "我的第二个marker", zIndexOffset: 1000}).addTo(map);
  // marker1.bindTooltip("我的第二个marker").openTooltip();
})


</script>

<style>

#map {
  height: 100vh;
}
</style>
