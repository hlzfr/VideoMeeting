module.exports = function(io, streams) {

  io.on('connection', function(client) {
    console.log('-- ' + client.id + ' joined --');
    client.emit('id', client.id);

    client.on('message', function (details) {
      var otherClient = io.sockets.connected[details.to];

      if (!otherClient) {
        return;
      }
        delete details.to;
        details.from = client.id;
        otherClient.emit('message', details);
    });
      
    client.on('readyToStream', function(options) {
      console.log('-- ' + client.id + ' is ready to stream --');
      
      streams.addStream(client.id, options.name); 

      var streamList = streams.getStreams();
      if(!streamList) {
        return;
      }
      for(var i = 0; i < streamList.length; i++) {
        var stream = streamList[i];
        if(stream.name == options.name && stream.id != client.id) { // 同一个房间
          var otherClient = io.sockets.connected[stream.id];
          if (!otherClient) {
            continue;
          }
          // 发送连接信息
          console.log('-- emit message '+'from '+client.id+', to '+stream.id+', type init');
          otherClient.emit("message", {
            from:client.id,
            type:"init"
          });
        }
      }
    });
    
    client.on('update', function(options) {
      streams.update(client.id, options.name);
    });

    function leave() {
      console.log('-- ' + client.id + ' left --');
      streams.removeStream(client.id);
    }

    client.on('disconnect', leave);
    client.on('leave', leave);
  });
};