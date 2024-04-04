<template>
<div class="canvas" ref="canvas">
    <img class="market-icon" :src="require('@/assets/images/market.png')" />
</div>
</template>

<script setup>
import * as echarts from 'echarts'
import {onMounted, ref, defineProps, watch} from 'vue'

const canvas = ref(null)
const props = defineProps(['marketData', 'chartConfig'])

const upColor = '#ec0000';
const downColor = '#00da3c';

const x_axis = ref([])
const y_series = ref([]);
const low_line = ref([])
const high_line = ref([])
const option = {
    title: {
        text: 'Stacked Line'
    },
    tooltip: {
        trigger: 'axis'
    },
    legend: {
        data: ['LastPrice', 'Union Ads', 'Video Ads', 'Direct', 'Search Engine']
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    toolbox: {
        feature: {
        saveAsImage: {}
        }
    },
    dataZoom: [
        {
        type: 'inside',
        xAxisIndex: [0, 1],
        start: 10,
        end: 100
        },
        {
        show: true,
        xAxisIndex: [0, 1],
        type: 'slider',
        bottom: 10,
        start: 10,
        end: 100
        }
    ],
    visualMap: {
        show: false,
        seriesIndex: 1,
        dimension: 6,
        pieces: [
        {
            value: 1,
            color: upColor
        },
        {
            value: -1,
            color: downColor
        }
        ],
        type: 'continuous',
        min: 0,
        max: 400
    },
    xAxis: {
        type: 'category',
        boundaryGap: false,
        data: x_axis.value
    },
    yAxis: {
        scale: true,
        splitArea: {
            show: true
        },
        type: 'value'
    },
    series: [
        {
            name: 'price',
            type: 'line',
            stack: 'Total',
            data: y_series.value
        },
        {
            name: 'rolling-mean',
            type: 'line',
            data: []
        },
        {
            name: 'low_line',
            type: 'line',
            data: low_line.value
        },
        {
            name: 'high_line',
            type: 'line',
            data: high_line.value
        }
    ]
};

const movingAverage = (data, windowSize) => {
    var movingAverages = [];
    for (let i = 0; i < windowSize - 1; i++) movingAverages.push(null)
    for (let i = 0; i < data.length - windowSize + 1; i++) {
        var sum = 0;
        for (let j = i; j < i + windowSize; j++) {
            sum += data[j];
        }
        var average = sum / windowSize;
        movingAverages.push(average);
    }
    return movingAverages;
}

watch(() => props.marketData, () => {
    x_axis.value = []
    y_series.value = []
    high_line.value = []
    low_line.value = []
    const marketList = props.marketData
    marketList.forEach(item => {
        x_axis.value.push(new Date(item.createTime))
        y_series.value.push(Number(item.lastPrice))
        high_line.value.push(Number(item.lastPrice * (1 + props.chartConfig.highRatio / 1000)))
        low_line.value.push(Number(item.lastPrice * (1 - props.chartConfig.lowRatio / 1000)))
    });
    if (x_axis.value.length == 0) return
    option.series[0].data = y_series.value
    option.series[1].data = movingAverage(y_series.value, props.chartConfig.window)
    option.series[2].data = low_line.value
    option.series[3].data = high_line.value
    option.xAxis.data = x_axis.value
    const myChart = echarts.init(canvas.value)
    option && myChart.setOption(option)  
})

watch(() => props.chartConfig.window, () => {
    option.series[1].data = movingAverage(y_series.value, props.chartConfig.window)
    const myChart = echarts.init(canvas.value);
    option && myChart.setOption(option);
})

watch(() => props.chartConfig.offset, () => {
    const len = option.series[0].data.length
    let circle = 200
    while (len - option.series[1].data.length < props.chartConfig.offset) {
        option.series[1].data.shift()
        circle --
        if (circle < 0) break
    }
    while (len - option.series[1].data.length > props.chartConfig.offset){
        option.series[1].data.unshift(null, 0)
        circle --
        if (circle < 0) break
    } 
    const myChart = echarts.init(canvas.value);
    option && myChart.setOption(option);
})

watch(() => props.chartConfig.lowRatio, () => {
    low_line.value = y_series.value.map((item) => item * (1 - props.chartConfig.lowRatio / 1000))
    option.series[2].data = low_line.value
    const myChart = echarts.init(canvas.value);
    option && myChart.setOption(option);
})

watch(() => props.chartConfig.highRatio, () => {
    high_line.value = y_series.value.map((item) => item * (1 + props.chartConfig.highRatio / 1000))
    option.series[3].data = high_line.value
    const myChart = echarts.init(canvas.value);
    option && myChart.setOption(option);
})

onMounted(() => {
    low_line.value = y_series.value.map(item =>  item * (1 - props.chartConfig.lowRatio / 1000))
    high_line.value = y_series.value.map(item => item * (1 + props.chartConfig.highRatio / 1000))
    option.series[0].data = y_series.value
    option.series[1].data = movingAverage(y_series.value, props.chartConfig.window)
    option.series[2].data = low_line.value
    option.series[3].data = high_line.value
    option.xAxis.data = x_axis.value
    const myChart = echarts.init(canvas.value);
    option && myChart.setOption(option);
})
</script>

<style scoped>
.canvas {
    width: 100%;
    animation-fill-mode: none;
}

.market-icon {
    position: relative;
    width: 30%;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
}
</style>
