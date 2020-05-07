   
var yate = YATE(document.getElementById('yateContainer'));
var yashe = YASHE(document.getElementById('yasheContainer'),{
  persisten:null,
  showShareButton:false,
  showValidateButton:false,
});
var yasme = YASME(document.getElementById('yasmeContainer'));

yate.setSize(null,505);
yashe.setSize(null,895);
yasme.setSize(null,895);

yate.setValue(
  `prefix :     <http://example.org/>

:ex1 :p 1 .
:ex2 :p 1,2 .
:ex3 :p 1 .
:ex4 :p 1,2 .
:ex5 :p 1 .
:ex6 :p 1,2 .
:ex7 :p 1 .
:ex8 :p 1,2 .`)

yashe.setValue(
  `PREFIX :       <http://example.org/>
:S{
 :p . 
}`)

yasme.setValue(
  `:ex1@:S,:ex2@:S,:ex3@:S,:ex4@:S,:ex5@:S,:ex6@:S,:ex7@:S,:ex8@:S`)



var validate = function(){

  console.log('ee')
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
              $('#log').text('pass')
              $('#table').remove();
              $('#validateZone').append(
                $('<div id="table" class="table-responsive">'+
                    '<table  class="table table-striped">'+ 
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
              console.log(error)
              $('#log').text(error.toString())
            });


 
}


function refresh(editor){
  setTimeout(()=>{
    editor.refresh();
  },200)
}


var params2Form = function(params) {
    let formData = new FormData()
    Object.keys(params).forEach(key => {
        formData.append(key,params[key])
    });
    
    return formData;
}
