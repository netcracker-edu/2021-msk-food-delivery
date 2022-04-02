import Config from "./Config";
import { commonFetch, patchFetch } from "../helpers/fetchers.js";

export default class DeliverySessionClient {
    auth;
    config = new Config();
    constructor(auth){
       this.auth = auth; 
    }

    async checkToken(){
        if (this.config.tokenExpired()) {
            await this.auth.refreshToken();
        }
    }

    async getOverallAmount(){
        await this.checkToken();
        return this.checkResponse(await commonFetch(this.config.DELIVERY_SESSIONS_AMOUNT_URL, 'GET', this.config.headersWithAuthorization(), null));
    }

    async fetchPage(page, size){
        await this.checkToken();
        return this.checkResponse(await commonFetch(this.buildPaginationQuery(page, size), 'GET', 
        this.config.headersWithAuthorization(), null));
    }   

    buildPaginationQuery(page, size){
        return this.config.DELIVERY_SESSIONS_HISTORY_URL + `?page=${page - 1}&size=${size}`;
    }

    async getSessionById(sessionId){
        await this.checkToken();
        return this.checkResponse(await commonFetch(this.config.DELIVERY_SESSION_URL + 
        `${sessionId}`, 'GET', this.config.headersWithAuthorization(), null));
    }

    async startSession(){
        await this.checkToken();
        this.checkResponse(await commonFetch(this.config.DELIVERY_SESSION_URL, 'POST', this.config.headersWithAuthorization(), null));
    }

    async finishSession(){
        await this.checkToken();
        const response = await patchFetch(this.config.DELIVERY_SESSION_URL, this.config.headersWithAuthorization(), null);
        return await this.handleFinishResponse(response);
    }

    async handleFinishResponse(response){
        if(response.status !== 200){
            const errMsgJson = await response.json();
            const errMsg = errMsgJson.message;
            const currentOrderId = errMsg.substring(errMsg.indexOf("id=") + 3);
            return currentOrderId;
        }

        return true; 
    }

    async getCurrentSession(){
        await this.checkToken();
        return this.checkResponse(await commonFetch(this.config.DELIVERY_SESSION_URL, 'GET', this.config.headersWithAuthorization(), null));
    }

    checkResponse(response){
        if(response.success){
            return response.data;
        } else {
            console.log(response.error);
        }
    }

}