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

    async getOrderById(id){
        this.checkToken();
        return await commonFetch(this.config.ORDER_URL + `/${id}`, 'GET', 
        this.config.headersWithAuthorization(), null);
    }

    async changeOrderStatus(orderId, newStatus){
        this.checkToken();
        return await patchFetch(this.config.ORDER_URL + `/${orderId}/status`, 
        this.config.headersWithAuthorization(), JSON.stringify({newStatus: newStatus}));
    }

    async changeRating(orderId, role, newRating){
        this.checkToken();
        return patchFetch(this.config.ORDER_URL + `/${orderId}/` + (role === 'COURIER' ? 'clientRating' : 'courierRating'),
        this.config.headersWithAuthorization(), JSON.stringify({rating: newRating}));
    }

    async getOverallOrdersAmount(){
        this.checkToken();
        return await commonFetch(this.config.ORDERS_AMOUNT_URL, 'GET', this.config.headersWithAuthorization(), null);
    }

    async fetchOrderPage(page, size){
        this.checkToken();
        return await commonFetch(this.buildPaginationQuery(page, size), 'GET', 
        this.config.headersWithAuthorization(), null);
    }   

    buildPaginationQuery(page, size){
        return this.config.ORDER_HISTORY_URL + `?page=${page - 1}&size=${size}`;
    }
}