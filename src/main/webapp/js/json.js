// 简单JSON格式化工具
var JsonUti = {
    //定义换行符
    n: "\n",
    //定义制表符
    t: "\t",
    //转换String
    convertToString: function(obj) {
    	if(JsonUti.__isArray(obj)){
    		return JsonUti.__writeArray(obj);
    	}
        return JsonUti.__writeObj(obj, 1);
    },
    // 写数组
    __writeArray: function(obj){
    	var str = "[";
    	$.each(obj, function(i,val){      
    		str += JsonUti.n + JsonUti.t + JsonUti.__writeObj(val, 2) + ",";   
    	}); 
    	str = str.substring(0, str.lastIndexOf(','));
    	str += JsonUti.n + "]";
    	return str;
    },
    //写对象
    __writeObj: function(obj //对象
    , level //层次（基数为1）
    , isInArray) { //此对象是否在一个集合内
        //如果为空，直接输出null
        if (obj == null) {
            return "null";
        }
        //为普通类型，直接输出值
        if (obj.constructor == Number || obj.constructor == Date || obj.constructor == String || obj.constructor == Boolean) {
            var v = obj.toString();
            var tab = isInArray ? JsonUti.__repeatStr(JsonUti.t, level - 1) : "";
            if (obj.constructor == String || obj.constructor == Date) {
                //时间格式化只是单纯输出字符串，而不是Date对象
                return tab + ("\"" + v + "\"");
            }
            else if (obj.constructor == Boolean) {
                return tab + v.toLowerCase();
            }
            else {
                return tab + (v);
            }
        }
        //写Json对象，缓存字符串
        var currentObjStrings = [];
        //遍历属性
        for (var name in obj) {
            var temp = [];
            //格式化Tab
            var paddingTab = JsonUti.__repeatStr(JsonUti.t, level);
            temp.push(paddingTab);
            //写出属性名
            temp.push("\"" + name + "\" : ");
            var val = obj[name];
            if (val == null) {
                temp.push("null");
            }
            else {
                var c = val.constructor;
                if (c == Array) { //如果为集合，循环内部对象
                    temp.push(JsonUti.n + paddingTab + "[" + JsonUti.n);
                    var levelUp = level + 2; //层级+2
                    var tempArrValue = []; //集合元素相关字符串缓存片段
                    for (var i = 0; i < val.length; i++) {
                        //递归写对象
                        tempArrValue.push(JsonUti.__writeObj(val[i], levelUp, true));
                    }
                    temp.push(tempArrValue.join("," + JsonUti.n));
                    temp.push(JsonUti.n + paddingTab + "]");
                }
                else if (c == Function) {
                    temp.push("[Function]");
                }
                else {
                    //递归写对象
                    temp.push(JsonUti.__writeObj(val, level + 1));
                }
            }
            //加入当前对象“属性”字符串
            currentObjStrings.push(temp.join(""));
        }
        return (level > 1 && !isInArray ? JsonUti.n: "") //如果Json对象是内部，就要换行格式化
        + JsonUti.__repeatStr(JsonUti.t, level - 1) + "{" + JsonUti.n //加层次Tab格式化
        + currentObjStrings.join("," + JsonUti.n) //串联所有属性值
        + JsonUti.n + JsonUti.__repeatStr(JsonUti.t, level - 1) + "}"; //封闭对象
    },
    __isArray: function(obj) {
        if (obj) {
            return obj.constructor == Array;
        }
        return false;
    },
    __repeatStr: function(str, times) {
        var newStr = [];
        if (times > 0) {
            for (var i = 0; i < times; i++) {
                newStr.push(str);
            }
        }
        return newStr.join("");
    }
};

// 将JSON格式化显示在html页面上
var JSONFormat = (function() {
	var _toString = Object.prototype.toString;
	function format(object, indent_count) {
		var html_fragment = '';
		switch (_typeof(object)) {
		case 'Null':
			0
			html_fragment = _format_null(object);
			break;
		case 'Boolean':
			html_fragment = _format_boolean(object);
			break;
		case 'Number':
			html_fragment = _format_number(object);
			break;
		case 'String':
			html_fragment = _format_string(object);
			break;
		case 'Array':
			html_fragment = _format_array(object, indent_count);
			break;
		case 'Object':
			html_fragment = _format_object(object, indent_count);
			break;
		}
		return html_fragment;
	}
	;
	function _format_null(object) {
		return '<span class="json_null">null</span>';
	}
	function _format_boolean(object) {
		return '<span class="json_boolean">' + object + '</span>';
	}
	function _format_number(object) {
		return '<span class="json_number">' + object + '</span>';
	}
	function _format_string(object) {
		if (0 <= object.search(/^http/)) {
			object = '<a href="' + object
					+ '" target="_blank" class="json_link">' + object + '</a>'
		}
		return '<span class="json_string">"' + object + '"</span>';
	}
	function _format_array(object, indent_count) {
		var tmp_array = [];
		for (var i = 0, size = object.length; i < size; ++i) {
			tmp_array.push(indent_tab(indent_count)
					+ format(object[i], indent_count + 1));
		}
		return '[\n' + tmp_array.join(',\n') + '\n'
				+ indent_tab(indent_count - 1) + ']';
	}
	function _format_object(object, indent_count) {
		var tmp_array = [];
		for ( var key in object) {
			tmp_array.push(indent_tab(indent_count)
					+ '<span class="json_key">"' + key + '"</span>:'
					+ format(object[key], indent_count + 1));
		}
		return '{\n' + tmp_array.join(',\n') + '\n'
				+ indent_tab(indent_count - 1) + '}';
	}
	function indent_tab(indent_count) {
		return (new Array(indent_count + 1)).join(' ');
	}
	function _typeof(object) {
		var tf = typeof object, ts = _toString.call(object);
		return null === object ? 'Null'
				: 'undefined' == tf ? 'Undefined'
						: 'boolean' == tf ? 'Boolean'
								: 'number' == tf ? 'Number'
										: 'string' == tf ? 'String'
												: '[object Function]' == ts ? 'Function'
														: '[object Array]' == ts ? 'Array'
																: '[object Date]' == ts ? 'Date'
																		: 'Object';
	}
	;
	function loadCssString() {
		var style = document.createElement('style');
		style.type = 'text/css';
		var code = Array.prototype.slice.apply(arguments).join('');
		try {
			style.appendChild(document.createTextNode(code));
		} catch (ex) {
			style.styleSheet.cssText = code;
		}
		document.getElementsByTagName('head')[0].appendChild(style);
	}
	loadCssString('.json_key{ color: purple;}', '.json_null{color: red;}',
			'.json_string{ color: #077;}', '.json_link{ color: #717171;}',
			'.json_array_brackets{}');
	var _JSONFormat = function(origin_data) {
		this.data = 'string' != typeof origin_data ? origin_data : JSON
				&& JSON.parse ? JSON.parse(origin_data) : eval('('
				+ origin_data + ')');
	};
	_JSONFormat.prototype = {
		constructor : JSONFormat,
		toString : function() {
			return format(this.data, 1);
		}
	}
	return _JSONFormat;
})();
