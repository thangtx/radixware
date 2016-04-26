/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var radixdoc = {
    initCollapsible: function() {
        var expand = function(object) {
            var content = $(object).parent().parent().parent().children('.content');
            if (content.css('display') === 'block') {
                content.css('display', 'none');
                $(object).text('▶');
            } else {
                content.css('display', 'block');
                $(object).text('▼');
            }
        };

        $(document).ready(function() {
            $('div.collapsible div.title div.action a').click(function() {
                expand(this);
            });
        });
    },
    createIndex: function() {

        var loadUnit = function(unit, path) {

            var unitDiv = $('<div class="layerUnit"/>');
            var unitUl = $('<ul/>');
            unitDiv.append($('<h2>All definitions</h2>'));
            unitDiv.append(unitUl);
            $('.definitions').empty();
            $('.definitions').append(unitDiv);

            for (var d in unit.children) {
                var definition = unit.children[d];
                var li = $('<li>' + '<img src="' + path + '/resources/' + definition.icon + '"/>' + definition.title + '</li>');
                var ref = path + '/' + definition.ref;
                li.bind('click', function(ref) {
                    return function() {
                        $('.definitions li.selected').attr('class', null);
                        $(this).attr('class', 'selected');
                        openPage(ref, true);
                    };
                }(ref));
                unitUl.append(li);
            }
        };

        var openPage = function(ref, history) {

            var frame = document.getElementById("documentationFrame");
            frame.contentWindow.location.replace(ref + ".html");

            if (history === true) {
                window.location.hash = ref;
            }
        };

        $(document).ready(function() {
            for (var l in units) {
                var layer = units[l];
                var layerDiv = $('<div class="layerUnit"/>');
                var layerUl = $('<ul/>');
                layerDiv.append($('<h2>' + layer.title + '</h2>'));
                layerDiv.append(layerUl);
                $('.units').append(layerDiv);

                for (var m in layer.children) {
                    var unit = layer.children[m];
                    var path = layer.subPath + '/' + unit.subPath;
                    var li = $('<li>' + '<img src="' + path + '/resources/' + unit.icon + '"/>' + unit.title + '</li>');

                    li.bind('click', function(unit, path) {
                        return function() {
                            $('.units li.selected').attr('class', null);
                            $(this).attr('class', 'selected');
                            loadUnit(unit, path);

                            var ref = path + '/' + unit.ref;
                            openPage(ref, true);
                        };
                    }(unit, path));
                    layerUl.append(li);
                }
            }

            var hash = window.location.hash;
            if (hash) {
                openPage(hash.substring(1, hash.length));
            }

            window.onpopstate = function(event) {
                var hash = window.location.hash;
                if (hash) {
                    openPage(hash.substring(1, hash.length));
                }
            };
        });
    }
};





