import Config from "./Config";

export default class UserClient {
  constructor() {
    this.config = new Config();
  }

  async register(userInfo) {
    console.log(userInfo);
    return fetch(this.config.SIGNUP_URL, {
      method: "POST",
      headers: {
        ...this.config.defaultHeaders(),
      },
      body: JSON.stringify(userInfo),
    })
      .then((response) => Promise.all([response, response.json()]))
      .then(([response, json]) => {
        if (!response.ok) {
          return { success: false, error: json };
        }
        return { success: true, data: json };
      })
      .catch((e) => {
        return this.handleError(e);
      });
  }

  handleError(error) {
    const err = new Map([
      [TypeError, "Can't connect to server."],
      [SyntaxError, "There was a problem parsing the response."],
      [Error, error.message],
    ]).get(error.constructor);
    console.log(err);
    return err;
  }
}
