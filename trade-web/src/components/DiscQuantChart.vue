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
const y_series = ref([])
const in_series = ref([])
const out_series = ref([])
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
    yAxis: [
        {
            scale: true,
            splitArea: {
                show: true
            },
            type: 'value',
            position: 'left'
        },
        {
            scale: true,
            splitArea: {
                show: true
            },
            type: 'value',
            position: 'right'
        },
    ],
    series: [
        {
            name: 'price',
            type: 'line',
            stack: 'Total',
            yAxisIndex: 0,
            data: y_series.value
        },
        {
            name: 'inside-disc',
            type: 'line',
            yAxisIndex: 1,
            data: in_series.value
        },
        {
            name: 'outside-disc',
            type: 'line',
            yAxisIndex: 1,
            data: out_series.value
        }
    ]
};

watch(() => props.marketData, () => {
    x_axis.value = []
    y_series.value = []
    in_series.value = []
    out_series.value = []
    const marketList = props.marketData
    marketList.forEach(item => {
        x_axis.value.push(new Date(item.createTime))
        y_series.value.push(item.lastPrice)
        in_series.value.push(item.insideDiff < -5000 ? 0 : item.insideDiff)
        out_series.value.push(item.outsideDiff < -5000 ? 0 : item.outsideDiff)
    });
    if (x_axis.value.length == 0) return
    option.series[0].data = y_series.value
    option.series[1].data = in_series.value
    option.series[2].data = out_series.value
    option.xAxis.data = x_axis.value
    const myChart = echarts.init(canvas.value)
    option && myChart.setOption(option)  
})

onMounted(() => {
    option.series[0].data = y_series.value
    option.series[1].data = in_series.value
    option.series[2].data = out_series.value
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
