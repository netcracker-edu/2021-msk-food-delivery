class Config {
  SCHEME = process.env.SCHEME ? process.env.SCHEME : "http";
  HOST = process.env.HOST ? process.env.HOST : "localhost";
  PORT = process.env.PORT ? process.env.PORT : "8080";
  API_VERS = "/api/v1/";
  BASE_PATH = `${this.SCHEME}://${this.HOST}:${this.PORT}${this.API_VERS}`;

  SIGNIN_URL = `${this.BASE_PATH}auth/signin`;
  SIGNOUT_URL = `${this.BASE_PATH}auth/signout`;
  SIGNUP_URL = `${this.BASE_PATH}auth/signup`;
  REFRESH_TOKEN_URL = `${this.BASE_PATH}auth/refresh`;
  PROFILE_URL = `${this.BASE_PATH}profile`;
  
  PRODUCTS_URL = `${this.BASE_PATH}products`;
  ORDER_URL = `${this.BASE_PATH}order`;
  ORDER_HISTORY_URL = `${this.PROFILE_URL}/orders`;
  ORDERS_AMOUNT_URL = `${this.ORDER_HISTORY_URL}/amount`;

  FILE_URL = `${this.BASE_PATH}file`;
  WAREHOUSE_URL = `${this.BASE_PATH}warehouse`;
  ACCESS_TOKEN = "accessToken";
  EXPIRATION = "expiration";

  DELIVERY_SESSIONS_HISTORY_URL = `${this.PROFILE_URL}/deliverySessions`;
  DELIVERY_SESSIONS_AMOUNT_URL = `${this.PROFILE_URL}/deliverySessions/amount`;
  DELIVERY_SESSION_URL = `${this.BASE_PATH}deliverySession/`;

  ORDER_URL = `${this.BASE_PATH}order`;
  ORDER_HISTORY_URL = `${this.PROFILE_URL}/orders`;
  ORDERS_AMOUNT_URL = `${this.ORDER_HISTORY_URL}/amount`;

  defaultHeaders() {
    return {
      "Content-Type": "application/json",
      "Accept": "application/json",
    };
  }

  headersWithAuthorization() {
    return {
      ...this.defaultHeaders(),
      "Authorization": localStorage.getItem(this.ACCESS_TOKEN),
    };
  }

  tokenExpired() {
    const expDate = Number(localStorage.getItem(this.EXPIRATION));
    if (expDate > Date.now()) {
      return false;
    }
    return true;
  }

  storeAccessToken(token) {
    localStorage.setItem(this.ACCESS_TOKEN, `Bearer ${token}`);
    localStorage.setItem(this.EXPIRATION, this.getExpiration(token));
  }

  getExpiration(token) {
    let encodedPayload = token ? token.split(".")[1] : null;
    if (encodedPayload) {
      encodedPayload = encodedPayload.replace(/-/g, "+").replace(/_/g, "/");
      const payload = JSON.parse(window.atob(encodedPayload));
      return payload?.exp ? payload?.exp * 1000 : 0;
    }
    return 0;
  }
}

export default Config;
