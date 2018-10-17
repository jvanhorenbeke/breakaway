var serverUrl = '';

//Compose template string
String.prototype.compose = (function (){
    var re = /\{{(.+?)\}}/g;
    return function (o){
        return this.replace(re, function (_, k){
            return typeof o[k] != 'undefined' ? o[k] : '';
        });
    }
}());
// --------------- Club Activities ---------------
var loadClubRankings = function() {
    var ytd = getUrlVar('ytd');
    var params = ytd != "" && (ytd == 'true') ? "?ytd=true" : "";

    var mainView = getUrlVar('main_view') == "" || (getUrlVar('main_view') != 'false')
    var mainViewParam = mainView ? "" : "?main_view=false";

    $.ajax({url: serverUrl + "/boards" + mainViewParam})
     .done(function (data) {
       $.each(JSON.parse(data), function(i, board) {
            loadSegmentLeaderBoard(board, params);
       })
     })
     .fail(function (jqXHR, textStatus) {
        console.log('unable to retrieve leaderboards for club');
     });
}
//--------------- Segment Leaderboard ---------------
var loadSegmentLeaderBoard = function(leaderboard, params) {
  $.ajax({url: serverUrl + '/' + leaderboard.name + params})
   .done(function (standings) {
       generateSegmentRankings(JSON.parse(standings), leaderboard);
   })
   .fail(function (jqXHR, textStatus) {
      console.log('unable to retrieve leaderboard: ' + leaderboard.name);
   });
};
// ------------------ Bind data to HTML elements ---------------------
var generateSegmentRankings = function(standings, leaderboard) {
    var table = $('#' + leaderboard.id).children('table');

    var jerseyImg = '<img src="./res/jersey/' + leaderboard.jersey + '.png" class="jersey" />';
    var row = '<tr>'+
      '<th scope="row">{{id}}</th>'+
      '<td>{{name}}</td>'+
      '<td>{{time}}</td>'+
      (leaderboard.hasGap ? '<td>{{gap}}</td>' : '') +
    '</tr>';

    if (standings.entries.length < 1) {
      table.append(row.compose({
          'id': '--',
          'name': '<i>No results</i>',
          'time': '--',
          'gap': '--',
          'activityId': '#'
      }));
    }

    var gap = 0;
    $.each(standings.entries, function(i, rider) {
        table.append(row.compose({
            'id': i+1,
            'name': i == 0 ? jerseyImg + rider.athlete_name : rider.athlete_name,
            'time': moment.utc(rider.elapsed_time*1000).format('mm:ss'),
            'gap': i == 0 ? '--' : moment.utc(1000*(rider.elapsed_time - gap)).format('mm:ss'),
            'activityId': rider.activity_id
        }));
        gap = i == 0 ? rider.elapsed_time : gap;
    });

    $('#' + leaderboard.id).show()
}

// ------------------ Util functions ---------------------
var metersToMiles = function(meters) {
    return Math.round(meters/1000*0.62137);
}

var metersToFeet = function(meters) {
    return Math.round(meters*3.28084);
}

var speedToMiles = function(metersPerSeconds) {
    metersPerHour = metersPerSeconds * 3600;
    return metersToMiles(metersPerHour);
}

var getUrlVar = function(key) {
	var result = new RegExp(key + "=([^&]*)", "i").exec(window.location.search);
	return result && unescape(result[1]) || "";
}
// -------------------------- document ready -----------------------------------
$(document).ready(function() {
    loadClubRankings();
});
