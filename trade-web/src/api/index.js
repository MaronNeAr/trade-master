import { get, post, getByToken, postByToken } from './request'

const HttpManager = {
  getCheckCodePic: () => post(`checkcode/pic`),
  verifyCheckCode: (params) => post(`checkcode/verify`, params),
  passwordAuth: (username) => post(`auth/oauth/token?client_id=TradeWebApp&client_secret=TradeWebApp&grant_type=password&username=` + username),
  checkToken: (params) => post(`auth/oauth/check_token`, params), 

  getBrokerages: () => get(`transaction/brokerages`),
  getDefaultBrokerageId: () => getByToken(`transaction/r/default_brokerage_id`),
  getAccountBalance: () => getByToken(`transaction/r/balance`),
  getBrokerageAccount: () => getByToken(`transaction/r/brokerage_account`),

  transfer: (params) => postByToken(`transaction/r/transfer`, params),
  withdraw: (params) => postByToken(`transaction/r/withdraw`, params),

  buy: (params) => postByToken(`transaction/r/buy`, params),
  sell: (params) => postByToken(`transaction/r/sell`, params),
  position: () => getByToken(`transaction/r/position`),
  details: (params) => postByToken(`transaction/r/details`, params),
  cancel: (params) => postByToken(`transaction/r/cancel`, params),

  getMarketData: (code) => get(`transaction/query/${code}`),
  getHistoryData: (code) => get(`transaction/history/${code}`),
  getMarketList: (params) => post(`transaction/query/securities`, params),
  getSSE50List: () => get(`transaction/query/securities/sse50`),

  getQuantMarketData: (params) => post(`transaction/market`, params),
  rollingWindowQuant: (params) => post(`transaction/quant/rolling-window`, params),
  inOutsideDiscQuant: (params) => post(`transaction/quant/in-outside-disc`, params)
}

export { HttpManager }
