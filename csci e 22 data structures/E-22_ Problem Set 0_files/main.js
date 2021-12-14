function setupPage(pageEl, pageNumber) {
  'use strict';

  var pageId = 'page' + pageNumber;

  pageEl.id = pageId;

  var bytes = document.querySelectorAll('#' + pageId + ' .byte');
  var byteInfo = document.querySelector('#' + pageId + ' .js-byte-info');

  for (var j = 0; j < bytes.length; j++) {
    bytes[j].onmouseover = function(e) {
      var el = e.currentTarget;

      var inRecord = el.getAttribute('data-in-record') != null;
      var byteIndex = el.getAttribute('data-byte-index');
      var recordName = el.getAttribute('data-record-name');
      var recordStart = el.getAttribute('data-record-start');
      var recordEnd = el.getAttribute('data-record-end');

      var inOffset = el.getAttribute('data-in-offset') != null;
      var offsetIndex = el.getAttribute('data-offset-index');
      var offsetValue = el.getAttribute('data-offset-value');

      // remove "inactive" class
      byteInfo.className = 'js-byte-info byte-info';

      var info = 'Byte ' + byteIndex;
      if (inRecord) {
        info += ', part of record ' + recordName + ' (starts at byte ' + recordStart + ' and ends at byte ' + recordEnd + ')';
      } else if (inOffset) {
        info += ', part of offset ' + offsetIndex + ' (contains the value <code>' + offsetValue + '</code>)';
      }

      byteInfo.innerHTML = info;
    };
  }
}

var cookie = {
  get: function(name, parseValue) {
    var cookies = document.cookie.split(';');
    var encodedName = encodeURIComponent(name);

    for (var i = 0; i < cookies.length; i++) {
      var parts = cookies[i].split('=');

      var key = parts[0].trim();
      if (key !== encodedName) {
        continue;
      }

      var value = decodeURIComponent(parts[1]);
      if (parseValue) {
        try {
            return JSON.parse(value);
        } catch (e) {
            return value;
        }
      } else {
        return value;
      }
    }
  },

  set: function(name, value, path) {
    var data = encodeURIComponent(name) + '=' + encodeURIComponent(value);
    if (path) {
      data += '; path=' + path;
    }

    document.cookie = data;
  }
};

(function() {
  'use strict';

  window.app = window.app || {};
  window.app.config = window.app.config || {};

  var pageCount = 0;

  var pages = document.getElementsByClassName('js-page');
  for (var i = 0; i < pages.length; i++) {
    setupPage(pages[i], pageCount++);
  }

  if (window.app.config.prefix) {
    var darkModeLinkEl = document.createElement('link');
    darkModeLinkEl.id = 'dark-stylesheet-link';
    darkModeLinkEl.href = window.app.config.prefix + '/css/dark.css';
    darkModeLinkEl.rel = 'stylesheet';

    var darkModeEls = document.getElementsByClassName('js-dark-toggle');
    for (var i = 0; i < darkModeEls.length; i++) {
      darkModeEls[i].onclick = function() {
        var darkModeOn = cookie.get('darkMode', true);
        if (darkModeOn) {
          var linkEl = document.getElementById('dark-stylesheet-link');
          if (linkEl) {
            linkEl.parentNode.removeChild(linkEl);
          }
        } else {
          document.head.appendChild(darkModeLinkEl);
        }

        cookie.set('darkMode', !darkModeOn, window.app.config.prefix);
      };
    }
  }
})();
