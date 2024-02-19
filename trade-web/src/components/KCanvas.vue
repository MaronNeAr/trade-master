<template>
<div class="canvas" ref="canvas">
    <img class="market-icon" :src="require('@/assets/images/market.png')" />
</div>
</template>

<script>
import * as echarts from 'echarts'

import {
    ref,
    watch
} from 'vue';
import {
    HttpManager
} from '@/api';

export default {
    props: {
        securityCode: String
    },
    setup(props) {
        const tradeData = ref([]);
        const canvas = ref(null);

        watch(() => props.securityCode, () => {
            getData();
        });

        async function getData() {
            if (props.securityCode == undefined || props.securityCode == null || props.securityCode == "") return
            const result = await HttpManager.getHistoryData(props.securityCode).catch(error => {
                console.log(error)
                return
            });
            if (!result?.success) {
                console.log(result?.message)
                return
            }
            tradeData.value = result.data
            renderChart()
        }

        getData();

        const renderChart = async() => {
            if (!canvas.value) return;
            const myChart = echarts.init(canvas.value);
            const upColor = '#ec0000';
            const upBorderColor = '#8A0000';
            const downColor = '#00da3c';
            const downBorderColor = '#fff';
            let tempDate = [];
            tradeData.value.forEach(item => {
                let year = "";
                if (item.date[0] < 5) year = "20" + item.date.slice(0, 2);
                else year = "19" + item.date.slice(0, 2);
                let date = new Date(year + "." + item.date.slice(2, 4) + "." + item.date.slice(4));
                tempDate.push([date.getFullYear() + "/" + (date.getMonth() + 1) + "/" + date.getDay(), item.open, item.close, item.low, item.high])
            });

            const data0 = splitData(tempDate);

            function splitData(rawData) {
                const categoryData = [];
                const values = [];
                for (var i = 0; i < rawData.length; i++) {
                    categoryData.push(rawData[i].splice(0, 1)[0]);
                    values.push(rawData[i]);
                }
                return {
                    categoryData: categoryData,
                    values: values
                };
            }

            function calculateMA(dayCount) {
                var result = [];
                for (var i = 0, len = data0.values.length; i < len; i++) {
                    if (i < dayCount) {
                        result.push('-');
                        continue;
                    }
                    var sum = 0;
                    for (var j = 0; j < dayCount; j++) {
                        sum += +data0.values[i - j][1];
                    }
                    result.push(sum / dayCount);
                }
                return result;
            }
            var option = {
                title: {
                    left: 0
                },
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'cross'
                    }
                },
                grid: {
                    left: '0px',
                    right: '0px',
                    top: '0px',
                    bottom: '0px',
                },
                xAxis: {
                    type: 'category',
                    data: data0.categoryData,
                    boundaryGap: false,
                    axisLine: {
                        onZero: false
                    },
                    splitLine: {
                        show: false
                    },
                    min: 'dataMin',
                    max: 'dataMax'
                },
                yAxis: {
                    scale: true,
                    splitArea: {
                        show: true
                    }
                },
                dataZoom: [{
                        type: 'inside',
                        start: 50,
                        end: 100
                    },
                    {
                        show: true,
                        type: 'slider',
                        top: '90%',
                        start: 50,
                        end: 100
                    }
                ],
                series: [{
                        name: '日K',
                        type: 'candlestick',
                        data: data0.values,
                        itemStyle: {
                            color: upColor,
                            color0: downColor,
                            borderColor: upBorderColor,
                            borderColor0: downBorderColor
                        },
                        markPoint: {
                            label: {
                                formatter: function (param) {
                                    return param != null ? Math.round(param.value) + '' : '';
                                }
                            },
                            data: [{
                                    name: 'Mark',
                                    coord: ['2013/5/31', 2300],
                                    value: 2300,
                                    itemStyle: {
                                        color: '#fff'
                                    }
                                },
                                {
                                    name: 'highest value',
                                    type: 'max',
                                    valueDim: 'highest'
                                },
                                {
                                    name: 'lowest value',
                                    type: 'min',
                                    valueDim: 'lowest'
                                },
                                {
                                    name: 'average value on close',
                                    type: 'average',
                                    valueDim: 'close'
                                }
                            ],
                            tooltip: {
                                formatter: function (param) {
                                    return param.name + '<br>' + (param.data.coord || '');
                                }
                            }
                        },
                        markLine: {
                            symbol: ['none', 'none'],
                            data: [
                                [{
                                        name: 'from lowest to highest',
                                        type: 'min',
                                        valueDim: 'lowest',
                                        symbol: 'circle',
                                        symbolSize: 10,
                                        label: {
                                            show: false
                                        },
                                        emphasis: {
                                            label: {
                                                show: false
                                            }
                                        }
                                    },
                                    {
                                        type: 'max',
                                        valueDim: 'highest',
                                        symbol: 'circle',
                                        symbolSize: 10,
                                        label: {
                                            show: false
                                        },
                                        emphasis: {
                                            label: {
                                                show: false
                                            }
                                        }
                                    }
                                ],
                                {
                                    name: 'min line on close',
                                    type: 'min',
                                    valueDim: 'close'
                                },
                                {
                                    name: 'max line on close',
                                    type: 'max',
                                    valueDim: 'close'
                                }
                            ]
                        }
                    },
                    {
                        name: 'MA5',
                        type: 'line',
                        data: calculateMA(5),
                        smooth: true,
                        lineStyle: {
                            opacity: 0.5
                        }
                    },
                    {
                        name: 'MA10',
                        type: 'line',
                        data: calculateMA(10),
                        smooth: true,
                        lineStyle: {
                            opacity: 0.5
                        }
                    },
                    {
                        name: 'MA20',
                        type: 'line',
                        data: calculateMA(20),
                        smooth: true,
                        lineStyle: {
                            opacity: 0.5
                        }
                    },
                    {
                        name: 'MA30',
                        type: 'line',
                        data: calculateMA(30),
                        smooth: true,
                        lineStyle: {
                            opacity: 0.5
                        }
                    }
                ]
            };
            option && myChart.setOption(option);
        }

        return {
            props,
            canvas
        }
    }
}
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
