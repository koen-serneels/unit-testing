$('document').ready(function(){
	searchOrdersForm = function() {
		var module= {};
		
		module.init = function(baseUrl){
			$("#orderdate").datepicker({ altFormat: "ddmmyyyy" });

			var th = document.createElement("th");
			var td = document.createElement("td");
			td.innerHTML = "<img src="+baseUrl+"/resources/css/images/details_open.png>";
			td.className = "center";			
			$("#orders thead tr").each( function () {
				this.insertBefore(th, this.childNodes[0] );
			} );
			
			$("#searchorders").click(function(e) {
				var orderName = $("#productname").val();
				var orderDescription = $("#productdescription").val();
				var orderDate = $.datepicker.formatDate('ddmmyy', $("#orderdate").datepicker("getDate"));
				
				var formData = {
						"orderDate": orderDate,
						"productName": orderName,
						"productDescription": orderDescription
				}
				
				$.getJSON(baseUrl+"/secured/searchOrders.json", formData, function(result) {
					$("#orders").show();
					
					var ordersTable = $('#orders').dataTable(
							{
								"bDestroy": true,
								"bAutoWidth": false,
								"aoColumnDefs": [{ "bSortable": false, "aTargets": [ 0 ] }],
								"aaSorting": [[1, 'asc']],
								"aaData": result,
								"aoColumns": [
								              { "mDataProp": "id" },
								              { "mDataProp": "name" },
								              { "mDataProp": "date" }
								          ]	
							});
					
					$("#orders tbody tr").each( function () {
						this.insertBefore(td.cloneNode(true),this.childNodes[0]);
					});
					
					$("#orders td img").click(function () {
						var ordersTableRow = this.parentNode.parentNode;
						if (this.src.match("details_close") ){
							this.src = baseUrl+"/resources/css/images/details_open.png";
							ordersTable.fnClose(ordersTableRow);
						}else{
							this.src = baseUrl+"/resources/css/images/details_close.png";
							ordersTable.fnOpen(ordersTableRow, module.formatDetails(ordersTable,ordersTableRow), "details");
						}
					});
				});
				e.preventDefault();
			});
		};
		
		module.formatDetails =  function (ordersTable, ordersTableRow){
			var table = '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">';
			var rowData = ordersTable.fnGetData(ordersTableRow);
			
			$.each(rowData.products, function(index, product){
				table += "<tr><td>Product name:"+ product.name + "</td></tr>";
				table += "<tr><td>Product description:"+ product.description + "</td></tr>";
				table += "<tr><td>Product price:"+ product.price + "</td></tr>";
				table += "<tr><td><hr/></td></tr>";
			});
			table += '</table>';
			return table;
		};
		
		return module;
	}();
});