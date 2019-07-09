const WebSocketServer = require('websocket').server;
const http = require('http');

const noop = () => {};

const server = http.createServer(noop);
server.listen(8080, noop);

const wsServer = new WebSocketServer({
  httpServer: server
});
wsServer.on('request', function (request) {
  const connection = request.accept(null, request.origin);
  connection.on('message', function () {
    connection.sendUTF('Hello, from Node.js');
  });
});
