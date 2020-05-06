var yate = YATE(document.getElementById('yateContainer'));
var yashe = YASHE(document.getElementById('yasheContainer'));
var yasme = YASME(document.getElementById('yasmeContainer'));
//yashe.setOption("fullScreen", true);

yate.setSize(null,550);
yashe.setSize(null,550);
yasme.setSize(null,100);


var validate = function(){


  let schemaContent = yashe.getValue();
  let dataContent = yate.getValue();
  let shapeMapContent = yasme.getValue();
  
  var params ={
  "activeTab": "#dataTextArea",
  "dataFormat": "TURTLE",
  "data": dataContent,
  "dataFormatTextArea": "TURTLE",
  "activeSchemaTab": "#schemaTextArea",
  "schemaEmbedded": false,
  "schemaFormat": "ShExC",
  "schema": schemaContent,
  "schemaFormatTextArea": "ShExC",
  "shapeMapActiveTab": "#shapeMapTextArea",
  "shapeMapFormat": "Compact",
  "shapeMap": shapeMapContent,
  "shapeMapFormatTextArea": "Compact",
  "schemaEngine": "ShEx",
  "triggerMode": "shapeMap",
}

let formData = params2Form(params);

    axios({
            method: 'post',
            url: 'http://rdfshape.weso.es:8080/api/schema/validate',
            data: formData,
            config: { headers: {'Content-Type': 'multipart/form-data' }},
            crossDomain: true
        }).then(response => response.data)
            .then((data) => {
          
              $('#table').remove();
              $('#validateZone').append(
                $('<div class="table-responsive">'+
                    '<table id="table" class="table table-striped">'+ 
                      '<thead id="thead" class="thead-dark">'+ 
                        '<tr>'+ 
                          '<th scope="col">Id</th>'+ 
                          '<th scope="col">Node</th>'+ 
                          '<th scope="col">Shape</th>'+ 
                          '<th scope="col">Details</th>'+ 
                        '</tr>'+ 
                      '</thead>'+ 
                      '<tbody id="tBody"/>'+
                    '</table>'+
                  '<div>')
              )
              
              Object.keys(data.shapeMap).map(s=>{
                var el = data.shapeMap[s];
                let succces = 'table-danger';
                if(el.status == 'conformant')succces = 'table-success';

                let id = $('<td>').text(s);
                let node = $('<td>').text(el.node,data.nodesPrefixMap);
                let shape =  $('<td>').text(el.shape,data.shapesPrefixMap);
                let details = $('<td>').append($('<details><pre>').text(el.reason));
   

                $('#tBody')
                  .append(
                    $('<tr class='+succces+'>')
                    .append(id)
                    .append(node)
                    .append(shape)
                    .append(details)
                  ) 
              })

                
            })
            .catch(function (error) {
              console.log("err")

            });


 
}



var params2Form = function(params) {
    let formData = new FormData()
    Object.keys(params).forEach(key => {
        formData.append(key,params[key])
    });
    
    return formData;
}