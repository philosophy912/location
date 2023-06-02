//map.js
// 换成本地的服务器js文件即可
let script = [
    'http://127.0.0.1:9000/bigemap.js/v2.1.0/bigemap.js',
    "ccc/bm.draw.min.js"
    //如果有更多的JS要引用 ，也一起放到这个数组中
];
export default new Promise(resolve => {
    let link = document.createElement("link");
    link.rel = "stylesheet";
    link.async = false;
    // 换成本地的服务器css文件即可
    link.href = "http://127.0.0.1:9000/bigemap.js/v2.1.0/bigemap.css";
    document.head.appendChild(link);
    // 添加新的css文件
    let draw = document.createElement("link");
    draw.rel = "stylesheet";
    draw.async = false;
    draw.href = "ccc/Bigemap.draw.css";
    document.head.appendChild(draw);
    let loads = script.map(v => {
        let script = document.createElement("script");
        script.type = "text/javascript";
        script.async = false;
        script.src = v;
        document.head.appendChild(script);
        return script;
    });
    let end = loads.pop();
    end.onload = resolve;
});
