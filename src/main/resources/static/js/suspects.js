window.onload = function() {
    $(function() {
        var reportList;
        var personList;
        var initialPersonList;
        var initialReportList;
        var accessLevelFill;

        var reportList2;
        var initialReportList2;

        document.getElementById("submitLinkForm").disabled = true; 

        // -------------------- PERSON FILTER FORM --------------------

        // Fetch persons list
        fetchPersons();
        
        function fetchPersons() {
            
            $.ajax({
                type: "POST",
                url: "/suspect/search/getPersons",
                contentType: "application/json",
                data: JSON.stringify('{"":""}'),
                dataType: 'json',
                success: function (data) {

                    personList = data.result;
                    initialPersonList = data.result;

                    for (var j = 0; j < personList.length; j++) {
                        $('#personId').append($('<option/>', {
                            value: personList[j].personId,
                            text: personList[j].personId,
                            id: personList[j].personId
                        }));
                    }

                },
                error: function (e) {

                    swal("Filter error!", e.responseText, e);
                }
            });
        }

        $("#personId").change(function() {
            fetchByPersonId($(this).val());
        });

        // Filter by person ID
        function fetchByPersonId(ID){ 

            var incidentField = document.getElementById('incidentURN').value;

            if ( ID != -1 && incidentField != -1 ) {
                document.getElementById("submitLinkForm").disabled = false; 
            } else {
                document.getElementById("submitLinkForm").disabled = true;
            }
        }

        $("#personSearchButton").click(function() {

            personList = initialPersonList;

            // Return fields to their default value
            $('#firstname').val('')
            $("#lastname").val('')
            $('#dateOfBirth').val('')
            $('#sex').val('-1');
            $('#ethnicOrigin').val('')
        });

        $("#firstname").change(function() {
            fetchByFirstname($(this).val());
        });

        // Filter by Firstname
        function fetchByFirstname(inputFirstname){ 

            var data = personList;

            filterList = $.grep(data, function(v) {
                return v.firstname == inputFirstname;
            })
            personList = filterList;
        }

        $("#lastname").change(function() {
            fetchByLastname($(this).val());
        });

        // Filter by Lastname
        function fetchByLastname(inputLastname){ 

            var data = personList;

            filterList = $.grep(data, function(v) {
                return v.lastname == inputLastname;
            })
            personList = filterList;
        }

        $("#dateOfBirth").change(function() {
            fetchByDateOfBirth($(this).val());
        });

        // Filter by Date of Birth
        function fetchByDateOfBirth(inputDateOfBirth){ 

            var data = personList;

            filterList = $.grep(data, function(v) {
                return v.dateOfBirth.split("T")[0] == inputDateOfBirth;
            })
            personList = filterList;
        }
        
        $("#sex").change(function() {
            fetchBySex($(this).val());
        });

        // Filter by Sex
        function fetchBySex(inputSex){ 

            var data = personList;

            filterList = $.grep(data, function(v) {
                return v.sex == inputSex;
            })
            personList = filterList;
        }

        $("#ethnicOrigin").change(function() {
            fetchByEthnicOrigin($(this).val());
        });

        // Filter by Ethnic Origin
        function fetchByEthnicOrigin(inputEthnicOrigin){ 

            var data = personList;

            filterList = $.grep(data, function(v) {
                return v.ethnicOrigin == inputEthnicOrigin;
            })
            personList = filterList;
        }

        $("#submitPersonForm").click(function() {

            if ( personList.length != 0 ) {
            
            // Constructing the fields of the person table
            let personList_selected = personList.map(function(obj) {
                return {
                    personId: obj.personId,
                    firstname: obj.firstname,
                    middleName: obj.middleName,
                    lastname: obj.lastname,
                    alias: obj.alias,
                    ethnicOrigin: obj.ethnicOrigin,
                    dateOfBirth: obj.dateOfBirth.split("T")[0],
                    sex: obj.sex,
                    weight: obj.weight,
                    height: obj.height
                }
            });

            var rows = personList_selected;
            var html = '<table>';
            html += '<tr>';
            for( var j in rows[0] ) {
            html += '<th>' + j + '</th>';
            }
            html += '</tr>';
            for( var i = 0; i < rows.length; i++) {
            html += '<tr>';
            for( var j in rows[i] ) {
            html += '<td>' + rows[i][j] + '</td>';
            }
            html += '</tr>';
            }
            html += '</table>';

            document.getElementById('person-table').innerHTML = html;
            
            // Return fields to their default value
            $('#firstname').val('')
            $("#lastname").val('')
            $('#dateOfBirth').val('')
            $('#sex').val('-1');
            $('#ethnicOrigin').val('')

            personList = initialPersonList;
        }
        else {
            swal("Alert!", "No results found.");
            personList = initialPersonList;
        }
        });
        
        // -------------------- INCIDENT FILTER FORM --------------------

        // Fetch reports list
        fetchReports();
        
        function fetchReports() {
            
            $.ajax({
                type: "POST",
                url: "/suspect/search/getReports",
                contentType: "application/json",
                data: JSON.stringify('{"":""}'),
                dataType: 'json',
                success: function (data) {

                    reportList2 = data.result;
                    initialReportList2 = data.result;

                    for (var j = 0; j < reportList2.length; j++) {
                        $('#incidentURN').append($('<option/>', {
                            value: reportList2[j].incidentURN,
                            text: reportList2[j].incidentURN,
                            id: reportList2[j].incidentURN
                        }));
                    }
            
                },
                error: function (e) {

                    swal("Filter error!", e.responseText, e);
                }
            });
        }

        $("#incidentURN").change(function() {
            fetchByIncidentId($(this).val());
        });

        // Filter by person ID
        function fetchByIncidentId(ID){ 

            var personField = document.getElementById('personId').value;

            if ( ID != -1 && personField != -1 ) {
                document.getElementById("submitLinkForm").disabled = false; 
            } else {
                document.getElementById("submitLinkForm").disabled = true;
            }
        }

        $("#securityAccessLevel").empty();
        $("#securityAccessLevel").append('<option value="0">-- select an option --</option>');
        
        // Disabling all fields except crime type
        document.getElementById("securityAccessLevel").disabled = true;
        document.getElementById("fromDate").disabled = true;
        document.getElementById("toDate").disabled = true;
        
        $("#crimeType").change(function() {  
            
            // Enable the security access level field
            document.getElementById("securityAccessLevel").disabled = false;

            fetchByCrimeType($(this).val()); 
        });

                // Fetch incidents list
                function fetchByCrimeType(dropdown) {
                    var levels = accessLevels.replaceAll('&quot;','"');
                    var json = {
                        "crimeType": dropdown,
                        "accessLevels": JSON.parse(levels)
                    };
                    $.ajax({
                        type: "POST",
                        contentType: "application/json",
                        url: "/suspect/search/byCrimeType",
                        data: JSON.stringify(json),
                        dataType: 'json',
                        cache: false,
                        timeout: 600000,
                        success: function (data) {
                            reportList = data;
        
                            var iterator =JSON.parse(levels);
        
                            $("#securityAccessLevel").empty();
                            $("#securityAccessLevel").append('<option value="0">-- select an option --</option>');
                            for (var i = 0; i < iterator.length; i++) {  
                                
                                $("#securityAccessLevel").append("<option value='"+iterator[i]+"'>"+iterator[i]+"</option>");
                            }

                            // Ensure fields are disabled
                            document.getElementById("fromDate").disabled = true;
                            document.getElementById("toDate").disabled = true;

                        },
                        error: function (e) {
                
                            swal("Filter error!", e.responseText, e);
                        }
                    });
                }

        $("#securityAccessLevel").change(function() {

            // Enable the fields once the security access level field is selected
            document.getElementById("fromDate").disabled = false;
            document.getElementById("toDate").disabled = false;

            fetchByAccessLevel($(this).val());
        });

        // Filter by Security Access Level
        function fetchByAccessLevel(ALdropdown){

            accessLevelFill = ALdropdown;
            var data = reportList.result;

            filterList = $.grep(data, function(v) {
                return v.securityAccessLevel == ALdropdown;
            })
            reportList = filterList;
        }

        $("#fromDate").change(function() {
            fetchByFromDate($(this).val());
        });

        // Filter by From Date
        function fetchByFromDate(minDate){

            var data = reportList;     

            filterList = $.grep(data, function(v) {
                return v.dateAndTime.split("T")[0] >= minDate;
            })
            reportList = filterList;
        }

        $("#toDate").change(function() {
            fetchByToDate($(this).val());
        });

        // Filter by To Date
        function fetchByToDate(maxDate){

            var data = reportList;     

            filterList = $.grep(data, function(v) {
                return v.dateAndTime.split("T")[0] <= maxDate;
            })
            reportList = filterList;
        }

        $("#submitIncidentForm").click(function() {

            initialReportList = reportList;
            
            if ( accessLevelFill == null ) {
                // alert("Please fill in the Crime Type & System Clearance fields !");
                swal("Error!", "Fields [Crime Type] and [System Clearance] are required to process filter.");
            } else {

            // Constructing the fields of the person table
            let reportList_selected = reportList.map(function(obj) {
                return {
                    incidentURN: obj.incidentURN,
                    badgeNumber: obj.badgeNumber,
                    crimeType: obj.crimeType,
                    date: obj.dateAndTime.split("T")[0],
                    time: obj.dateAndTime.split("T")[1],
                    securityAccessLevel: obj.securityAccessLevel
                }
            });

            var rows = reportList_selected;
            var html = '<table>';
            html += '<tr>';
            for( var j in rows[0] ) {
            html += '<th>' + j + '</th>';
            }
            html += '</tr>';
            for( var i = 0; i < rows.length; i++) {
            html += '<tr>';
            for( var j in rows[i] ) {
            html += '<td>' + rows[i][j] + '</td>';
            }
            html += '</tr>';
            }
            html += '</table>';
            document.getElementById('incident-table').innerHTML = html;
            
            // Return fields to their default value
            $('#crimeType').val('-1');
            $("#securityAccessLevel").empty();
            $("#securityAccessLevel").append('<option value="0">-- select an option --</option>');
            $('#fromDate').val('')
            $("#toDate").val('')

            // Disable back the fields
            document.getElementById("securityAccessLevel").disabled = true;
            document.getElementById("fromDate").disabled = true;
            document.getElementById("toDate").disabled = true;

            // Empty previous table results
            accessLevelFill = null;
            personList = null;
            reportList = null;

            if ( initialReportList.length == 0 ) {
                swal("Alert!", "No results found.");
                // reportList = initialReportList;
            }

            }
        });
    });
};