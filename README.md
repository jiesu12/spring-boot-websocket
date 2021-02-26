# Spring Boot WebSocket

Use SockJS to create WebSocket.

## SockJS

WebSocket is handled by SockJS on both server side (Spring boot) and client side (browser).
SockJS client create a special URL pattern to call SockJS server.
The URL looks like `http://localhost/websocket/123/aaaaa/websocket`. This pattern is used to create SockJS sessions.
SockJS server sends heartbeats to client periodically to decide whether to keep the session alive.

When SockJS client initializing the connection, it will try multiple protocols.
First it will try WebSocket. If WebSocket failed, SockJS client will try other protocols, such as xhr streaming.
What is why you may sometime see browser trying to connect to different URLs ending with `xhr_streaming`,
`xdr_streaming`, etc.

## Client Side

Npm install:

```
npm i sockjs-client stompjs
```

Establish connection and subscribe:

```typescript
import * as SockJS from "sockjs-client";
import * as Stomp from "stompjs";

const socket = new SockJS("/websocket");
const stomp = Stomp.over(socket);
stomp.debug = null; // don't show console logs
stomp.connect({}, () => {
  stomp.subscribe("/topic/test", (message) => {
    console.log(message.body);
  });
});
```

## Nginx

```
server {
    server_name localhost;
    listen 80;

    location / {
      proxy_pass http://192.168.1.100:9000;

      # Following are for WebSocket:
      proxy_http_version 1.1;
      proxy_set_header Upgrade $http_upgrade;
      proxy_set_header Connection "Upgrade";
      proxy_set_header Host $host;
    }
}
```
