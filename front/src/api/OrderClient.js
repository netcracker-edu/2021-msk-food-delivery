import Config from "./Config";
import { patchFetch, commonFetch } from "../helpers/fetchers";

export default class OrderClient{
    auth;
    constructor(auth) {
        this.auth = auth;
        this.config = new Config();
    }

    async checkToken(){
        if (this.config.tokenExpired()) {
            await this.auth.refreshToken();
        }
    }

    async calculateTotalPrice(cartItems, coords, warehouseId) {
      if (this.config.tokenExpired()) {
        await this.auth.refreshToken();
      }
      return commonFetch(this.config.ORDER_URL+"/price",
                    "POST",
                    this.config.headersWithAuthorization(),
                    JSON.stringify(
                        {
                          "geo" : coords,
                          "warehouseId" : warehouseId,
                          "products" : cartItems
                        }));
    }

    async getOrderById(id){
        await this.checkToken();
        return commonFetch(this.config.ORDER_URL + `/${id}`, 'GET',
        this.config.headersWithAuthorization(), null);
    }

    async changeOrderStatus(orderId, newStatus){
        await this.checkToken();
        return patchFetch(this.config.ORDER_URL + `/${orderId}/status`,
        this.config.headersWithAuthorization(), JSON.stringify({newStatus: newStatus}));
    }

    async changeRating(orderId, role, newRating){
        await this.checkToken();
        return patchFetch(this.config.ORDER_URL + `/${orderId}/` + (role === 'COURIER' ? 'clientRating' : 'courierRating'),
        this.config.headersWithAuthorization(), JSON.stringify({rating: newRating}));
    }

    async getOverallOrdersAmount(){
        await this.checkToken();
        return commonFetch(this.config.ORDERS_AMOUNT_URL, 'GET', this.config.headersWithAuthorization(), null);
    }

    async fetchOrderPage(page, size){
        await this.checkToken();
        return commonFetch(this.buildPaginationQuery(page, size), 'GET',
        this.config.headersWithAuthorization(), null);
    }

    async fetchFilteredOrderPage(warehouseId, status, page, size){
        await this.checkToken();
        return commonFetch(this.buildFilterQuery(warehouseId, status, page, size), 'GET',
        this.config.headersWithAuthorization(), null);
    }

    async fetchFilteredAmount(warehouseId, status){
        await this.checkToken();
        return commonFetch(this.buildFilterAmountQuery(warehouseId, status), 'GET',
        this.config.headersWithAuthorization(), null);
    }

    buildPaginationQuery(page, size){
        return this.config.ORDER_HISTORY_URL + `?page=${page - 1}&size=${size}`;
    }

    buildFilterQuery(warehouseId, status, page, size){
        return this.config.FILTER_ORDERS_URL + `?warehouseId=${warehouseId}&status=${status}&page=${page - 1}&size=${size}`;
    }

    buildFilterAmountQuery(warehouseId, status){
        return this.config.AMOUNT_FILTER_ORDERS_URL + `?warehouseId=${warehouseId}&status=${status}`;
    }

    async getOrdersFromSession(sessionId){
        await this.checkToken();
        return await commonFetch(this.config.DELIVERY_SESSION_URL + `${sessionId}/orders`, 'GET',
        this.config.headersWithAuthorization(), null);
    }

    async getCurrentOrder(){
        await this.checkToken();
        return await commonFetch(this.config.ORDER_URL, 'GET', this.config.headersWithAuthorization(), null);
    }

    async orderCheckout(totalPrice, address, cartItems) {
        await this.checkToken();
        const coords = { "lat" : address.coord[0], "lon" : address.coord[1]};
        const orderData = {
          "overallCost" : totalPrice.overallCost,
          "discount" : totalPrice.discount,
          "highDemandCoeff": totalPrice.highDemandCoeff,
          "warehouseId" : address.warehouseId,
          "geo" : coords,
          "products" : cartItems,
          "address" : address.fullAddress
        };
        return await commonFetch(
                this.config.ORDER_URL,
                'POST',
                this.config.headersWithAuthorization(),
                JSON.stringify(orderData));
    }
}
