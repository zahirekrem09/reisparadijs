class HttpClient {
    constructor() {
        this.interceptors = {
            request: [],
            response: []
        };
        this.defaultHeaders = {
            'Content-Type': 'application/json'
        };
        this.refreshTokenUrl = '/api/auth/refresh'   // Refresh token endpoint
        this.accessToken = null;
        this.refreshToken = null;
        this.isRefreshing = false;
        this.failedQueue = [];
    }



    setTokens(accessToken, refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    // Token'ları yenileme
    async refreshAccessToken() {
        try {
            const response = await fetch(this.refreshTokenUrl, {
                method: 'POST',
                headers: this.defaultHeaders,
                body: JSON.stringify({ refreshToken: localStorage.getItem('refreshToken') })
            });
            const data = await response.json();
            if (!response.ok) {
                throw new Error(`Failed to refresh token: ${data.message}`);
            }
            this.accessToken = data.accessToken;
            this.refreshToken = data.refreshToken;
            localStorage.setItem('accessToken', data.accessToken);
            localStorage.setItem('refreshToken', data.refreshToken);
            return data.accessToken;
        } catch (error) {
            console.error('Error refreshing access token:', error);
            throw error;
        }
    }

    // HTTP isteği yapma
    async request(url, method = 'GET', body = null, headers = {}) {
        let options = {
            method: method,
            headers: { ...this.defaultHeaders, ...headers },
            body: body ? JSON.stringify(body) : null
        };

        // Request interceptor'ları çalıştır
        this.interceptors.request.forEach(interceptor => {
            options = interceptor(options);
        });

        if (this.accessToken) {
            options.headers['Authorization'] = `Bearer ${this.accessToken}`;
        }
        try {
            const response = await fetch(url, options);

            if (!response.ok) {
                if (response.status === 401) { // Unauthorized, token refresh işlemi yapılacak
                    if (!this.isRefreshing) {
                        this.isRefreshing = true;
                        try {
                            const newToken = await this.refreshAccessToken();
                            this.isRefreshing = false;
                            this.failedQueue.forEach(prom => prom.resolve(newToken));
                            this.failedQueue = [];
                            return this.request(url, method, body, headers); // İlk isteği yeniden dene
                        } catch (error) {
                            this.failedQueue.forEach(prom => prom.reject(error));
                            this.failedQueue = [];
                            throw error;
                        }
                    } else {
                        return new Promise((resolve, reject) => {
                            this.failedQueue.push({ resolve, reject });
                        }).then(() => this.request(url, method, body, headers));
                    }
                } else {
                    const errorData = await response.json();
                    const error = new Error(`HTTP error! Status: ${response.status}`);
                    error.data = errorData;
                    throw error;
                }
            }
            let responseData = await response.json();

            // ** response interceptor call
            this.interceptors.response.forEach(interceptor => {
                responseData = interceptor(responseData);
            });

            return responseData;
        } catch (error) {
            console.error('HTTP Client Error:', error);
            throw error;
        }
    }

    get(url, headers = {}) {
        return this.request(url, 'GET', null, headers);
    }

    post(url, body, headers = {}) {
        return this.request(url, 'POST', body, headers);
    }

    put(url, body, headers = {}) {
        return this.request(url, 'PUT', body, headers);
    }

    delete(url, headers = {}) {
        return this.request(url, 'DELETE', null, headers);
    }

    addRequestInterceptor(interceptor) {
        this.interceptors.request.push(interceptor);
    }

    addResponseInterceptor(interceptor) {
        this.interceptors.response.push(interceptor);
    }

    setDefaultHeaders(headers) {
        this.defaultHeaders = { ...this.defaultHeaders, ...headers };
    }
}

const httpClient = new HttpClient();


export default httpClient;
