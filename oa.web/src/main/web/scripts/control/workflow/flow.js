function checkFormRule(frm) {
    try {
        var oAll = frm.all;
        var oItem, s, r;
        for (i = 0;
             i < oAll.length;
             i++) {
            oItem = oAll[i];
            s = oItem.tagName.toLowerCase();
            if (s == "input" || s == "select") {
                if ("rule"in oItem) {
                    r = new RegExp(oItem.rule);
                    if (!r.test(oItem.value)) {
                        alert(oItem.msg);
                        oItem.focus();
                        if (s == "input") {
                            oItem.select();
                        }
                        return false;
                    }
                }
            }
        }
        return true;
    }
    catch (e) {
        alert(e);
        return false;
    }
}

function mergecell(tb) {
    var iRowCnt = tb.rows.length;
    if (iRowCnt <= 1) {
        return;
    }
    var iPreIndex = 1;
    var sPreText = tb.rows(iPreIndex).cells(0).innerText;
    var sCurText = "";
    for (var i = 2;
         i < iRowCnt;
         i++) {
        sCurText = tb.rows(i).cells(0).innerText;
        if (sCurText == sPreText) {
            tb.rows(iPreIndex).cells(0).rowSpan++;
            tb.rows(i).deleteCell(0);
        } else {
            iPreIndex = i;
            sPreText = sCurText;
        }
    }
}


if (!Array.prototype.push) {
    Array.prototype.push = function array_push() {
        for (var i = 0;
             i < arguments.length;
             i++) {
            this[this.length] = arguments[i];
        }
        return this.length;
    }
}
;
if (!Array.prototype.pop) {
    Array.prototype.pop = function array_pop() {
        lastElement = this[this.length - 1];
        this.length = Math.max(this.length - 1, 0);
        return lastElement;
    }
}
;

var _ToolBoxManager = [];


function setToolBoxTopMost(AToolBox) {
    for (var i = 0;
         i < _ToolBoxManager.length;
         i++) {
        _ToolBoxManager[i].setZIndex(3000);
    }
    AToolBox.setZIndex(3001);
}
;

var _SHAPE = [];
_SHAPE["roundrect"] = [];
_SHAPE["rect"] = [];
_SHAPE["oval"] = [];
_SHAPE["diamond"] = [];
_SHAPE["line"] = [];
_SHAPE["polyline"] = [];
_SHAPE["polylinenolabel"] = [];
_SHAPE["oval"]["demo"] = '<v:Oval id="demoOval" title="圆形" style="position:relative;left:0px;top:0px;width:100px;height:40px;z-index:9" strokecolor="red" strokeweight="1">' + '<v:shadow id="demoOvalShadow" on="T" type="single" color="#b3b3b3" offset="5px,5px"/>' + '<v:extrusion id="demoOvalExt" on="false" backdepth="20" />' + '<v:fill id="demoOvalFill" type="gradient" color="white" color2="white" />' + '<v:TextBox id="demoOvalText" inset="2pt,5pt,2pt,5pt" style="text-align:center; color:red; font-size:9pt;">示例</v:TextBox>' + '</v:Oval>';
_SHAPE["oval"]["val"] = '<v:Oval id="{id}" title="{title}" sc="{sc}" fsc="{fsc}" st="{st}" typ="Proc" style="position:absolute;left:{l};top:{t};width:{w};height:{h};z-index:{z}"" strokecolor="{sc}" strokeweight="{sw}" ondblclick=\'editProc(this.id);\' >' + '<v:shadow on="{shadowenable}" type="single" color="{shadowcolor}" offset="5px,5px"/>' + '<v:extrusion on="{3denable}" backdepth="{3ddepth}" />' + '<v:fill type="gradient" color="{sc1}" color2="{sc2}" />' + '<v:TextBox id="{id}Text" inset="2pt,5pt,2pt,5pt" style="text-align:center; color:{tc}; font-size:{fs};">{text}</v:TextBox>' + '</v:Oval>';
_SHAPE["rect"]["demo"] = '<v:rect id="demoRect" title="方形" style="backgroup-color:#f0f0f0;z-index:0;position:relative;width:100px;height:40px;left:0px;top:0px;" strokecolor="blue" strokeweight="1">' + '  <v:shadow on="T" type="single" color="#b3b3b3" offset="5px,5px"/>' + '  <v:extrusion on="false" backdepth="20" />' + '  <v:fill type="gradient" color="white" color2="white" />' + '  <v:TextBox inset="2pt,5pt,2pt,5pt" style="text-align:center; color:blue; font-size:9pt;">示例</v:TextBox>' + '</v:rect>';
_SHAPE["rect"]["val"] = '<v:rect id="{id}" title="{title}" sc="{sc}" fsc="{fsc}" st="{st}" typ="Proc" style="z-index:{z};position:absolute;width:{w};height:{h};left:{l};top:{t};" strokecolor="{sc}" strokeweight="{sw}" ondblclick=\'editProc(this.id);\'>' + '  <v:shadow on="{shadowenable}" type="single" color="{shadowcolor}" offset="5px,5px"/>' + '  <v:extrusion on="{3denable}" backdepth="{3ddepth}" />' + '  <v:fill type="gradient" color="#dddddd" color2="#dddddd" />' + '  <v:TextBox id="{id}Text" inset="2pt,5pt,2pt,5pt" style="text-align:center; color:{tc}; font-size:{fs};">{text}</v:TextBox>' + '</v:rect>';
_SHAPE["roundrect"]["demo"] = '<v:RoundRect id="demoRoundRect" title="圆角形" style="position:relative;left:0px;top:0px;width:100px;height:40px;z-index:9"" strokecolor="blue" strokeweight="1">' + '<v:shadow id="demoRoundRectShadow" on="T" type="single" color="#b3b3b3" offset="5px,5px"/>' + '<v:extrusion id="demoRoundRectExt" on="false" backdepth="20" />' + '<v:fill id="demoRoundRectFill" type="gradient" color="white" color2="white" />' + '<v:TextBox id="demoRoundRectText" inset="2pt,5pt,2pt,5pt" style="text-align:center; color:blue; font-size:9pt;">示例</v:TextBox>' + '</v:RoundRect>';
_SHAPE["roundrect"]["val"] = '<v:RoundRect id="{id}" title="{title}" sc="{sc}" fsc="{fsc}" st="{st}" typ="Proc" style="position:absolute;left:{l};top:{t};width:{w};height:{h};z-index:{z}"" strokecolor="{sc}" strokeweight="{sw}" ondblclick=\'editProc(this.id);\'>' + '<v:shadow on="{shadowenable}" type="single" color="{shadowcolor}" offset="5px,5px"/>' + '<v:extrusion on="{3denable}" backdepth="{3ddepth}" />' + '<v:fill type="gradient" color="{sc1}" color2="{sc2}" />' + '<v:TextBox id="{id}Text" inset="2pt,5pt,2pt,5pt" style="text-align:center; color:{tc}; font-size:{fs};">{text}</v:TextBox>' + '</v:RoundRect>';
_SHAPE["diamond"]["demo"] = '<v:shape id="demoDiamond" title="菱形" type="#diamond" style="position:relative;left:0px;top:0px;width:100px;height:50px;z-index:9" strokecolor="blue" strokeweight="1">' + '<v:shadow on="T" type="single" color="#b3b3b3" offset="5px,5px"/>' + '<v:extrusion on="false" backdepth="20" />' + '<v:fill type="gradient" color="white" color2="white" />' + '<v:TextBox inset="5pt,10pt,5pt,5pt" style="text-align:center; color:blue; font-size:9pt;">示例</v:TextBox>' + '</v:shape>';
_SHAPE["diamond"]["val"] = '<v:shape type="#diamond" id="{id}" title="{title}" sc="{sc}" fsc="{fsc}" st="{st}" typ="Proc" style="position:absolute;width:{w};height:{h};left:{l};top:{t};z-index:{z}" strokecolor="{sc}" strokeweight="{sw}" ondblclick=\'editProc(this.id);\'>' + '  <v:shadow on="{shadowenable}" type="single" color="{shadowcolor}" offset="5px,5px"/>' + '  <v:extrusion on="{3denable}" backdepth="{3ddepth}" />' + '  <v:fill type="gradient" color="{sc1}" color2="{sc2}" />' + '  <v:TextBox id="{id}Text" inset="2pt,10pt,2pt,5pt" style="text-align:center; color:{tc}; font-size:{fs};">{text}</v:TextBox>' + '</v:shape>';
_SHAPE["line"]["demo"] = '<v:line id="demoLine" title="直线" style="z-index:0;position:relative;" from="0,0" to="100,0" strokecolor="blue" strokeweight="1">' + '<v:stroke id="demoLineArrow" StartArrow="" EndArrow="Classic"/>' + '<v:TextBox inset="5pt,1pt,5pt,5pt" style="text-align:center; color:blue; font-size:9pt;"></v:TextBox>' + '</v:line>'
_SHAPE["line"]["val"] = '<v:line id="{id}" title="{title}" sc="{sc}" fsc="{fsc}" typ="Step" style="z-index:{z};position:absolute;" {pt} strokecolor="{sc}" strokeweight="{sw}" onmousedown=\'objFocusedOn(this.id);\' ondblclick=\'editStep(this.id);\'>' + '<v:stroke id="{id}Arrow" StartArrow="{sa}" EndArrow="{ea}"/>' + '<v:TextBox id="{id}Text" inset="5pt,1pt,5pt,5pt" style="text-align:center; color:blue; font-size:9pt;">{cond}</v:TextBox>' + '</v:line>';
_SHAPE["polyline"]["demo"] = '<v:PolyLine id="demoPolyLine" title="折角线" filled="false" Points="0,20 50,0 100,20" style="z-index:0;position:relative;" strokecolor="blue" strokeweight="1">' + '<v:stroke id="demoPolyLineArrow" StartArrow="" EndArrow="Classic"/>' + '<v:TextBox inset="5pt,1pt,5pt,5pt" style="text-align:center; color:blue; font-size:9pt;"></v:TextBox>' + '</v:PolyLine>';
_SHAPE["polyline"]["val"] = '<v:PolyLine id="{id}" title="{title}" sc="{sc}" fsc="{fsc}" typ="Step" filled="false" Points="{pt}" style="z-index:{z};position:absolute;" strokecolor="{sc}" strokeweight="{sw}" onmousedown=\'objFocusedOn(this.id);\' ondblclick=\'editStep(this.id);\'>' + '<v:stroke id="{id}Arrow" StartArrow="{sa}" EndArrow="{ea}"/>' + '</v:PolyLine>' + '<div id={lid} title="{desc}" style="width:{w};height:{h};overflow:hidden;position:absolute;top:{y};left:{x};font-size:12px;z-index:1;" onmousedown="objFocusedOn(\'{id}\');">{text}</div>';
_SHAPE["polylinenolabel"]["val"] = '<v:PolyLine id="{id}" title="{title}" sc="{sc}" fsc="{fsc}" typ="Step" filled="false" Points="{pt}" style="z-index:{z};position:absolute;" strokecolor="{sc}" strokeweight="{sw}" onmousedown=\'objFocusedOn(this.id);\' ondblclick=\'editStep(this.id);\'>' + '<v:stroke id="{id}Arrow" StartArrow="{sa}" EndArrow="{ea}"/>' + '</v:PolyLine>';
function getShapeDemo(AName) {
    return _SHAPE[AName.toLowerCase()]["demo"];
}
function getShapeVal(AName) {
    return _SHAPE[AName.toLowerCase()]["val"];
}
function stuffShape(AStr, arr) {
    var re = /\{(\w+)\}/g;
    return AStr.replace(re, function (a, b) {
        return arr[b]
    });
}
document.write('<v:shapetype id="diamond" coordsize="12,12" path="m 6,0 l 0,6,6,12,12,6 x e"/>');


function ifRepeatProc(fromX, fromY, fromW, fromH, toX, toY, toW, toH) {
    return(fromX + fromW >= toX) && (fromY + fromH >= toY) && (toX + toW >= fromX) && (toY + toH >= fromY);
}
function TLabel(id) {
    this.ID = id;
    this.Text = "";
    this.Desc = "";
    this.Step = null;
    this.X = 0;
    this.Y = 0;
    this.Width = "40px";
    this.Height = "20px";
    this.bgColor = "#ffffff";
}


var _FLOW = new TTopFlow("");
var _TOOLTYPE = "point";
var _CURRENTX = _CURRENTY = 0;
var _FOCUSTEDOBJ = null;
var _ZOOM = 1;
var _MOVEOBJ = null;
var _MOVETYPE = "";
var _DOLOG = [];
var _DOLOGINDEX = -1;
var _strPt1 = "";
var _strPt2 = "";
var _strSltPt = "";
var _strLine1 = "";
var _strLine2 = "";
var _strSltLine = "";
var _PointOrLine;
var isSelectPoint = 0;
var isSelectLine = 0;
var _clkPx = 0;
var _clkPy = 0;
var ptMoveType = "";
var oOval = null;
var _logMoveType = "";
var _MOVELINEOBJ = null;
function objFocusedOn(objId) {
    objFocusedOff();
    _FOCUSTEDOBJ = document.getElementById(objId);
    if (_FOCUSTEDOBJ != null) {
        _FOCUSTEDOBJ.StrokeColor = _FOCUSTEDOBJ.fsc;
        _FOCUSTEDOBJ.StrokeWeight = 1.2;
    }
    var x = (event.x + document.body.scrollLeft) / _ZOOM;
    var y = (event.y + document.body.scrollTop) / _ZOOM;
    _clkPx = x / 4 * 3 + "pt";
    _clkPy = y / 4 * 3 + "pt";
    if (_FOCUSTEDOBJ.tagName == "PolyLine") {
        modifyStepShape(_FOCUSTEDOBJ, x, y);
    }
    stuffProp();
}
function createOval(x, y) {
    oOval = document.createElement("v:oval");
    oOval.style.position = "absolute";
    oOval.style.width = "6px";
    oOval.style.height = "6px"
    oOval.style.left = x;
    oOval.style.top = y;
    oOval.fillcolor = "red";
    oOval.strokecolor = "red";
    document.body.appendChild(oOval);
}
function getMinMod(c, m) {
    var iMin = 0;
    var k = 0;
    c = Math.round(c);
    for (var i = c - m / 2;
         i < c + m / 2;
         i++) {
        if (i % m == 0) {
            if (k == 0) {
                iMin = i;
            }
            else {
                if (Math.abs(i - c) < Math.abs(iMin - c)) {
                    iMin = i;
                }
            }
            k++;
        }
    }
    return iMin;
}
function getNearPt(oProc, x, y) {
    var objProc = document.getElementById(oProc.ID)
    var fromW = parseInt(objProc.style.width);
    var fromH = parseInt(objProc.style.height);
    var fromX = parseInt(objProc.style.left);
    var fromY = parseInt(objProc.style.top);
    var arrX = new Array();
    var arrY = new Array();
    var arrPos = new Array(0, 0.25, 0.5, 0.75, 1);
    var nX, nY, m, n, nearPt, poX, poY;
    arrX[0] = fromX;
    arrX[1] = fromX + fromW / 4;
    arrX[2] = fromX + fromW / 2;
    arrX[3] = fromX + fromW * 3 / 4;
    arrX[4] = fromX + fromW;
    arrY[0] = fromY;
    arrY[1] = fromY + fromH / 4;
    arrY[2] = fromY + fromH / 2;
    arrY[3] = fromY + fromH * 3 / 4;
    arrY[4] = fromY + fromH;
    m = 0;
    n = 0;
    for (var i = 0;
         i < 4;
         i++) {
        Math.abs(x - arrX[i]) < Math.abs(x - arrX[i + 1]) ? m = m : m = i + 1;
        Math.abs(y - arrY[i]) < Math.abs(y - arrY[i + 1]) ? n = n : n = i + 1;
    }
    if (m > 0 && m < 4 && n > 0 && n < 4) {
        if (m == 3) {
            m = 4;
        }
        else {
            m = 0;
        }
        if (n == 3) {
            n = 4;
        }
        else {
            n = 0;
        }
    }
    nX = arrX[m];
    nY = arrY[n];
    poX = arrPos[m];
    poY = arrPos[n];
    nearPt = nX * 3 / 4 + "pt," + nY * 3 / 4 + "pt|~|" + poX + "," + poY
    return nearPt;
}
function alertPro(obj) {
    for (var i in
        obj) {
        alert(i + '\n' + obj[i])
    }
}
function modifyStepShape(objstep, x, y) {
    _MOVEOBJ = document.getElementById(objstep.id);
    _MOVELINEOBJ = new TStep(_FLOW);
    _MOVELINEOBJ.clone(_FLOW.getStepByID(objstep.id));
    _MOVEOBJ = _FLOW.getStepByID(objstep.id);
    if (_MOVEOBJ.Label) {
        document.getElementById(_MOVEOBJ.Label.ID).style.backgroundColor = '#dddddd';
    }
    var strPt = _MOVEOBJ.Points;
    var aryPt = strPt.split(',');
    var nPt = aryPt.length - 1;
    _strPt2 = "";
    _strPt1 = "";
    _strLine2 = "";
    _strLine1 = "";
    for (i = 0;
         i <= nPt;
         i = i + 2) {
        var m = aryPt[i].substr(0, aryPt[i].length - 2) * 4 / 3;
        var n = aryPt[i + 1].substr(0, aryPt[i + 1].length - 2) * 4 / 3;
        var sqrta = Math.sqrt((x - m) * (x - m) + (y - n) * (y - n));
        if (isSelectPoint == 0 && sqrta <= 10) {
            _PointOrLine = 0;
            isSelectPoint = 1;
            _MOVETYPE = "line_m";
            _strSltPt = aryPt[i] + ',' + aryPt[i + 1];
            _clkPx = aryPt[i];
            _clkPy = aryPt[i + 1];
            if (i == 0) {
                ptMoveType = "from";
            }
            if (i == nPt - 1) {
                ptMoveType = "to";
            }
        }
        else {
            if (isSelectPoint == 1) {
                _strPt2 = _strPt2 + ',' + aryPt[i] + ',' + aryPt[i + 1];
            }
            else {
                _strPt1 = _strPt1 + ',' + aryPt[i] + ',' + aryPt[i + 1];
            }
        }
        if (i <= nPt - 3) {
            var r = aryPt[i + 2].substr(0, aryPt[i + 2].length - 2) * 4 / 3;
            var s = aryPt[i + 3].substr(0, aryPt[i + 3].length - 2) * 4 / 3;
            if ((Math.abs(x * (n - s) + y * (r - m) + (m * s - n * r)) / Math.sqrt((n - s) * (n - s) + (r - m) * (r - m)) <= 5) && (isSelectLine == 0) && sqrta > 10 && isSelectPoint == 0) {
                _PointOrLine = 1;
                _MOVETYPE = "line_m";
                isSelectLine = 1;
                _strSltLine = aryPt[i] + ',' + aryPt[i + 1] + ',' + aryPt[i + 2] + ',' + aryPt[i + 3];
                _clkPx = x / 4 * 3 + "pt";
                _clkPy = y / 4 * 3 + "pt";
            }
            else {
                if (isSelectLine == 1) {
                    if (i <= nPt - 3) {
                        _strLine2 = _strLine2 + ',' + aryPt[i + 2] + ',' + aryPt[i + 3];
                    }
                }
                else {
                    _strLine1 = _strLine1 + "," + aryPt[i] + ',' + aryPt[i + 1];
                }
            }
        }
    }
    if (_strPt1 != '') {
        _strPt1 = _strPt1.substr(1) + ',';
    }
    if (_strLine1 != '') {
        _strLine1 = _strLine1.substr(1) + ",";
    }
}
function objFocusedOff() {
    if (_FOCUSTEDOBJ != null) {
        _FOCUSTEDOBJ.StrokeColor = _FOCUSTEDOBJ.sc;
        _FOCUSTEDOBJ.StrokeWeight = 1;
        var oLabel = document.getElementById('lab' + _FOCUSTEDOBJ.id);
        if (oLabel) {
            oLabel.style.backgroundColor = '';
        }
    }
    _FOCUSTEDOBJ = null;
    isSelectPoint = 0;
    isSelectLine = 0;
    ptMoveType = "";
    oOval = null;
    return;
}
function deleteObj(ObjId) {
    var obj = document.getElementById(ObjId);
    if (obj == null) {
        return false;
    }
    if (obj.typ != "Proc" && obj.typ != "Step") {
        return false;
    }
    if (obj.typ == "Proc" && (obj.st == "begin" || obj.st == "end" || obj.st == "first")) {
        alert("不能删除！一个流程图中，[开始节点],[第一个节点],[结束结点]是必需的");
        return false;
    }
    var objVML = obj.typ == "Proc" ? _FLOW.getProcByID(_FOCUSTEDOBJ.id) : _FLOW.getStepByID(_FOCUSTEDOBJ.id);
    if (obj.typ == "Step" && objVML.FromProc == 'begin') {
        alert('不能删除开始节点联线！');
        return false;
    }
    if (confirm("确定要删除[" + objVML.Text + "]吗？")) {
        objFocusedOff();
        if (obj.typ == "Proc") {
            var Proc = _FLOW.getProcByID(ObjId);
            _FLOW.deleteProcByID(ObjId);
            pushLog("delproc", Proc);
        }
        else {
            var Step = _FLOW.getStepByID(ObjId);
            _FLOW.deleteStepByID(ObjId);
            pushLog("delstep", Step);
        }
        _FLOW.Modified = true;
        DrawAll();
    }
}
function changeProcID(OldID, NewID) {
    var Step;
    for (var i = 0;
         i < _FLOW.Steps.length;
         i++) {
        Step = _FLOW.Steps[i];
        if (Step.FromProc == OldID) {
            Step.FromProc = NewID;
        }
        if (Step.ToProc == OldID) {
            Step.ToProc = NewID;
        }
    }
}
function saveStepsToProc(obj) {
    for (var i = 0;
         i < _FLOW.Steps.length;
         i++) {
        var oStep = _FLOW.Steps[i];
        if (oStep.FromProc == obj.id || oStep.ToProc == obj.id) {
        }
    }
}
function changeProcPos(obj) {
    for (var i = 0;
         i < _FLOW.Steps.length;
         i++) {
        Step = _FLOW.Steps[i];
        if (Step.FromProc == obj.id || Step.ToProc == obj.id) {
            objStepHTML = document.getElementById(Step.ID);
            if (Step.ShapeType == "Line") {
                Step.getPath();
                objStepHTML.from = Step.FromPoint;
                objStepHTML.to = Step.ToPoint;
            }
            else {
                if (Step.ShapeType == "PolyLine") {
                    var strPt = "";
                    var arrPt = Step.Points.split(",");
                    var objWidth = obj.style.width;
                    var objHeight = obj.style.height;
                    var objX = obj.style.left;
                    var objY = obj.style.top;
                    var strMoveType = _MOVETYPE;
                    objWidth = objWidth.substr(0, objWidth.length - 2);
                    objHeight = objHeight.substr(0, objHeight.length - 2);
                    objX = objX.substr(0, objX.length - 2) * 1;
                    objY = objY.substr(0, objY.length - 2) * 1;
                    if (_MOVETYPE == "") {
                        strMoveType = _logMoveType;
                    }
                    if (Step.FromProc == obj.id) {
                        strPt = (objX + objWidth * Step.fromRelX) * 3 / 4 + "pt," + (objY + objHeight * Step.fromRelY) * 3 / 4 + "pt"
                        for (var j = 2;
                             j < arrPt.length;
                             j++) {
                            strPt = strPt + "," + arrPt[j]
                        }
                    }
                    if (Step.ToProc == obj.id) {
                        for (var j = 0;
                             j < arrPt.length - 2;
                             j++) {
                            strPt = strPt + arrPt[j] + ","
                        }
                        strPt = strPt + (objX + objWidth * Step.toRelX) * 3 / 4 + "pt," + (objY + objHeight * Step.toRelY) * 3 / 4 + "pt"
                    }
                    Step.Points = strPt;
                    objStepHTML.outerHTML = Step.toStringWithoutLabel();
                }
            }
        }
    }
    _logMoveType = "";
}
function editProc(objId) {
}
function editStep(objId) {
    var oldobj = new TStep(_FLOW), newobj = new TStep(_FLOW);
    var step = _FLOW.getStepByID(objId);
    oldobj.clone(step);
    var x = (event.x + document.body.scrollLeft) / _ZOOM;
    var y = (event.y + document.body.scrollTop) / _ZOOM;
    var strPt = step.Points;
    var aryPt = strPt.split(',');
    var nPt = aryPt.length - 1;
    isSelectPoint = 0;
    _strPt2 = "";
    _strPt1 = "";
    for (i = 0;
         i <= nPt;
         i = i + 2) {
        var m = aryPt[i].substr(0, aryPt[i].length - 2) * 1.333;
        var n = aryPt[i + 1].substr(0, aryPt[i + 1].length - 2) * 1.333;
        var sqrta = Math.sqrt((x - m) * (x - m) + (y - n) * (y - n));
        if (isSelectPoint == 0 && sqrta <= 10) {
            _strSltPt = aryPt[i] + ',' + aryPt[i + 1];
        }
        else {
            if (isSelectPoint == 1) {
                _strPt2 = _strPt2 + ',' + aryPt[i] + ',' + aryPt[i + 1];
            }
            else {
                _strPt1 = _strPt1 + ',' + aryPt[i] + ',' + aryPt[i + 1];
            }
        }
    }
    if (_strPt1 != '') {
        _strPt1 = _strPt1.substr(1);
    }
    step.Points = _strPt1 + _strPt2;
    document.getElementById(step.ID).outerHTML = step.toStringWithoutLabel();
    objFocusedOn(objId);
    _FLOW.Modified = true;
}
function beforePropChange(oItem) {
    if ("rule"in oItem) {
        r = new RegExp(oItem.rule);
        if (!r.test(oItem.value)) {
            alert(oItem.msg);
            oItem.focus();
            if (oItem.tagName.toLowerCase() == "input") {
                oItem.select();
            }
            return false;
        }
    }
    return true;
}
function setPropID(oItem) {
    var obj = null, obj2 = null;
    obj = _FOCUSTEDOBJ.typ == "Proc" ? _FLOW.getProcByID(_FOCUSTEDOBJ.id) : _FLOW.getStepByID(_FOCUSTEDOBJ.id);
    var oldID = obj.ID;
    if (oldID == oItem.value) {
        return;
    }
    obj2 = _FLOW.getProcByID(oItem.value);
    if (obj2 == null) {
        obj2 = _FLOW.getStepByID(oItem.value);
    }
    if (obj2 != null) {
        alert("编号[" + oItem.value + "-" + obj2.Text + "]已经存在！请重新输入！");
        oItem.focus();
        oItem.select();
        return;
    }
    document.all(_FOCUSTEDOBJ.id + "Text").id = oItem.value + "Text";
    obj.ID = oItem.value;
    _FOCUSTEDOBJ.id = oItem.value;
    if (_FOCUSTEDOBJ.typ == "Proc") {
        changeProcID(oldID, obj.ID);
    }
    pushLog("editprop", {"obj": obj, "prop": "ID", "_old": oldID, "_new": obj.ID});
}
function setPropText(oItem) {
    var obj = _FOCUSTEDOBJ.typ == "Proc" ? _FLOW.getProcByID(_FOCUSTEDOBJ.id) : _FLOW.getStepByID(_FOCUSTEDOBJ.id);
    if (obj.Text == oItem.value) {
        return;
    }
    if (_FLOW.isExitName(oItem.value)) {
        /*alert('节点和连线名称不允许重复！');
         oItem.value = obj.Text;
         return;*/
    }
    var oldValue = obj.Text;
    obj.Text = oItem.value;
    if (obj.ObjType == "Proc") {
        DrawAll();
    }
    else {
        document.getElementById('lab' + _FOCUSTEDOBJ.id).innerHTML = oItem.value;
    }
    objFocusedOn(obj.ID);
    pushLog("editprop", {"obj": obj, "prop": "Text", "_old": oldValue, "_new": obj.Text});
}
function setPropCond(oItem) {
    var obj = _FLOW.getStepByID(_FOCUSTEDOBJ.id);
    if (obj.Cond == oItem.value) {
        return;
    }
    var oldValue = obj.Cond;
    obj.Cond = oItem.value;
    document.all(_FOCUSTEDOBJ.id + "Text").innerHTML = oItem.value;
    pushLog("editprop", {"obj": obj, "prop": "Cond", "_old": oldValue, "_new": obj.Cond});
}
function setProcType(oItem) {
    var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
    if (obj.ProcType == oItem.value) {
        return;
    }
    var oldValue = obj.ProcType;
    var oldShape = obj.ShapeType;
    obj.ProcType = oItem.value;
    var strImageType = '';
    switch (oItem.value) {
        case'submit':
        case'finish':
        case'modify':
        case'andsign':
        case'unandsign':
        case'connectsign':
            strImageType = 'roundrect';
            break;
        case'fork':
            strImageType = 'diamond';
            break;
        case'andjoin':
        case'unandjoin':
            strImageType = 'rect';
            break;
        default:
            break;
    }
    obj.ShapeType = strImageType;
    DrawAll();
    objFocusedOn(obj.ID);
    pushLog("editprop", {"obj": obj, "prop": "ProcType", "_old": oldValue, "_new": obj.ProcType, "_oldShape": oldShape, "_newShape": obj.ShapeType});
}
function setSelectExit(oItem) {
    var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
    var oldValue = obj.SelectExit;
    var newValue = 0;
    if (oItem.checked) {
        newValue = 1;
    }
    obj.SelectExit = newValue;
    pushLog("editprop", {"obj": obj, "prop": "SelectExit", "_old": oldValue, "_new": obj.newValue});
}
function setAllowAgent(oItem) {
    var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
    var oldValue = obj.AllowAgent;
    var newValue = 0;
    if (oItem.checked) {
        newValue = 1;
    }
    obj.AllowAgent = newValue;
    pushLog("editprop", {"obj": obj, "prop": "AllowAgent", "_old": oldValue, "_new": obj.newValue});
}
function setNotifyInstanceCreator(oItem) {
    var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
    var oldValue = obj.NotifyInstanceCreator;
    var newValue = 0;
    if (oItem.checked) {
        newValue = 1;
    }
    obj.NotifyInstanceCreator = newValue;
    pushLog("editprop", {"obj": obj, "prop": "NotifyInstanceCreator", "_old": oldValue, "_new": obj.newValue});
}
function setInstanceReturnMode(oItem) {
    var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
    var oldValue = obj.InstanceReturnMode;
    var newValue = 0;
    if (oItem.checked) {
        newValue = 1;
    }
    obj.InstanceReturnMode = newValue;
    pushLog("editprop", {"obj": obj, "prop": "InstanceReturnMode", "_old": oldValue, "_new": obj.newValue});
}
function setSkipHandler(oItem) {
    var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
    var oldValue = obj.SkipHandler;
    var newValue = 0;
    if (oItem.checked) {
        newValue = 1;
    }
    obj.SkipHandler = newValue;
    pushLog("editprop", {"obj": obj, "prop": "SkipHandler", "_old": oldValue, "_new": obj.newValue});
}
function setSuspendInstance(oItem) {
    var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
    var oldValue = obj.SuspendInstance;
    var newValue = 0;
    if (oItem.checked) {
        newValue = 1;
    }
    obj.SuspendInstance = newValue;
    pushLog("editprop", {"obj": obj, "prop": "SuspendInstance", "_old": oldValue, "_new": obj.newValue});
}
function setTimeLimit(oItem) {
    var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
    var oldValue = obj.TimeLimit;
    if (obj.TimeLimit == oItem.value) {
        return;
    }
    obj.TimeLimit = oItem.value;
    pushLog("editprop", {"obj": obj, "prop": "TimeLimit", "_old": oldValue, "_new": obj.TimeLimit});
}
function setWarnType() {
    var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
    var oldValue = obj.WarnType;
    var isNote = document.getElementById('note').checked;
    var isSMS = document.getElementById('sms').checked;
    var isEmail = document.getElementById('email').checked;
    var strWarnType = ',';
    if (isNote) {
        strWarnType += 'note';
    }
    if (isSMS) {
        strWarnType += 'sms';
    }
    if (isEmail) {
        strWarnType += 'email';
    }
    strWarnType.replace(',', '');
    obj.WarnType = strWarnType;
    pushLog("editprop", {"obj": obj, "prop": "WarnType", "_old": oldValue, "_new": obj.WarnType});
}
function setHandlerFetchType() {
    var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
    var oldValue = obj.HandlerFetchType;
    var isRandom = document.getElementById('Random').checked;
    var isIdleMost = document.getElementById('IdleMost').checked;
    var isTaskLeast = document.getElementById('TaskLeast').checked;
    var strHandlerFetchType = ',';
    if (isRandom) {
        strHandlerFetchType += 'Random';
    }
    if (isIdleMost) {
        strHandlerFetchType += 'IdleMost';
    }
    if (isTaskLeast) {
        strHandlerFetchType += 'TaskLeast';
    }
    strHandlerFetchType.replace(',', '');
    obj.HandlerFetchType = strHandlerFetchType;
    pushLog("editprop", {"obj": obj, "prop": "HandlerFetchType", "_old": oldValue, "_new": obj.HandlerFetchType});
}
function setFlowNodeAction() {
    var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
    var oldValue = obj.FlowNodeAction;
    var flowNodeAction = ',';
    if (document.getElementById('passAction').checked) {
        flowNodeAction += 'passAction' + ",";
    }
    if (document.getElementById('returnAction').checked) {
        flowNodeAction += 'returnAction' + ",";
    }
    if (document.getElementById('rejectAction').checked) {
        flowNodeAction += 'rejectAction' + ",";
    }
    if (document.getElementById('discardAction').checked) {
        flowNodeAction += 'discardAction' + ",";
    }
    if (document.getElementById('completeAction').checked) {
        flowNodeAction += 'completeAction' + ",";
    }
    //flowNodeAction.replace(',', '');
    obj.FlowNodeAction = flowNodeAction;
    pushLog("editprop", {"obj": obj, "prop": "FlowNodeAction", "_old": oldValue, "_new": obj.FlowNodeAction});
}
function setExecuter(oItem) {
    var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
    var oldValue = obj.Executer;
    if (obj.Executer == oItem.value) {
        return;
    }
    obj.Executer = oItem.value;
    pushLog("editprop", {"obj": obj, "prop": "Executer", "_old": oldValue, "_new": obj.Executer});
}
function setSuspendDescription(oItem) {
    var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
    var oldValue = obj.SuspendDescription;
    if (obj.SuspendDescription == oItem.value) {
        return;
    }
    obj.SuspendDescription = oItem.value;
    pushLog("editprop", {"obj": obj, "prop": "SuspendDescription", "_old": oldValue, "_new": obj.Executer});
}
function setHandlerOnFlowNode(oItem) {

    var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
    var oldValue = obj.HandlerOnFlowNode;
    if (obj.HandlerOnFlowNode == oItem.value) {
        return;
    }
    obj.HandlerOnFlowNode = oItem.value;
    pushLog("editprop", {"obj": obj, "prop": "HandlerOnFlowNode", "_old": oldValue, "_new": obj.HandlerOnFlowNode});
}
function setUserPropertyOnEntity(oItem) {

    var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
    var oldValue = obj.UserPropertyOnEntity;
    if (obj.UserPropertyOnEntity == oItem.value) {
        return;
    }
    obj.UserPropertyOnEntity = oItem.value;
    pushLog("editprop", {"obj": obj, "prop": "UserPropertyOnEntity", "_old": oldValue, "_new": obj.UserPropertyOnEntity});
}
function setFlowNodeTag(oItem) {
    var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
    var oldValue = obj.FlowNodeTag;
    if (obj.FlowNodeTag == oItem.value) {
        return;
    }
    obj.FlowNodeTag = oItem.value;
    pushLog("editprop", {"obj": obj, "prop": "FlowNodeTag", "_old": oldValue, "_new": obj.FlowNodeTag});
}
function setOrganizationId(oItem) {
    var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
    var oldValue = obj.OrganizationId;
    if (obj.OrganizationId == oItem.value) {
        return;
    }
    obj.OrganizationId = oItem.value;
    pushLog("editprop", {"obj": obj, "prop": "OrganizationId", "_old": oldValue, "_new": obj.OrganizationId});
}
function setRoleId(oItem) {
    var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
    var oldValue = obj.RoleId;
    if (obj.RoleId == oItem.value) {
        return;
    }
    obj.RoleId = oItem.value;
    pushLog("editprop", {"obj": obj, "prop": "RoleId", "_old": oldValue, "_new": obj.RoleId});
}
function setUseRootNode(oItem) {
    var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
    var oldValue = obj.UseRootNode;
    if (obj.UseRootNode == oItem.value) {
        return;
    }
    obj.UseRootNode = oItem.value;
    pushLog("editprop", {"obj": obj, "prop": "UseRootNode", "_old": oldValue, "_new": obj.UseRootNode});
}
function setOrganizationProperty(oItem) {
    var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
    var oldValue = obj.OrganizationProperty;
    if (obj.OrganizationProperty == oItem.value) {
        return;
    }
    obj.OrganizationProperty = oItem.value;
    pushLog("editprop", {"obj": obj, "prop": "OrganizationProperty", "_old": oldValue, "_new": obj.OrganizationProperty});
}
function setRoleProperty(oItem) {
    var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
    var oldValue = obj.RoleProperty;
    if (obj.RoleProperty == oItem.value) {
        return;
    }
    obj.RoleProperty = oItem.value;
    pushLog("editprop", {"obj": obj, "prop": "RoleProperty", "_old": oldValue, "_new": obj.RoleProperty});
}
function setHandlerFetchCount(oItem) {
    var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
    var oldValue = obj.HandlerFetchCount;
    if (obj.HandlerFetchCount == oItem.value) {
        return;
    }
    obj.HandlerFetchCount = oItem.value;
    pushLog("editprop", {"obj": obj, "prop": "HandlerFetchCount", "_old": oldValue, "_new": obj.HandlerFetchCount});
}
function setActivity(oItem) {
    var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
    var oldValue = obj.Activity;
    if (obj.Activity == oItem.value) {
        return;
    }
    obj.Activity = oItem.value;
    pushLog("editprop", {"obj": obj, "prop": "Activity", "_old": oldValue, "_new": obj.Activity});
}
function setEntityStatusTag(oItem) {
    var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
    var oldValue = obj.EntityStatusTag;
    if (obj.EntityStatusTag == oItem.value) {
        return;
    }
    obj.EntityStatusTag = oItem.value;
    pushLog("editprop", {"obj": obj, "prop": "EntityStatusTag", "_old": oldValue, "_new": obj.EntityStatusTag});
}
function setAidancer(oItem) {
    var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
    var oldValue = obj.Aidancer;
    if (obj.Aidancer == oItem.value) {
        return;
    }
    obj.Aidancer = oItem.value;
    pushLog("editprop", {"obj": obj, "prop": "Aidancer", "_old": oldValue, "_new": obj.Aidancer});
}
function setEvent(oItem) {
    var obj = _FLOW.getStepByID(_FOCUSTEDOBJ.id);
    var oldValue = obj.Event;
    if (obj.Event == oItem.value) {
        return;
    }
    obj.Event = oItem.value;
    pushLog("editprop", {"obj": obj, "prop": "Event", "_old": oldValue, "_new": obj.Event});
}
function setRamusTag(oItem) {
    var obj = _FLOW.getStepByID(_FOCUSTEDOBJ.id);
    var oldValue = obj.Event;
    if (obj.RamusTag == oItem.value) {
        return;
    }
    obj.RamusTag = oItem.value;
    pushLog("editprop", {"obj": obj, "prop": "RamusTag", "_old": oldValue, "_new": obj.RamusTag});
}
function setStatusTag(oItem) {
    var obj = _FLOW.getStepByID(_FOCUSTEDOBJ.id);
    var oldValue = obj.StatusTag;
    if (obj.StatusTag == oItem.value) {
        return;
    }
    obj.StatusTag = oItem.value;
    pushLog("editprop", {"obj": obj, "prop": "StatusTag", "_old": oldValue, "_new": obj.StatusTag});
}
function setCond(oItem) {
    var obj = _FLOW.getStepByID(_FOCUSTEDOBJ.id);
    var oldValue = obj.Cond;
    if (obj.Cond == oItem.value) {
        return;
    }
    obj.Cond = oItem.value;
    pushLog("editprop", {"obj": obj, "prop": "Cond", "_old": oldValue, "_new": obj.Cond});
}
function setNotifyContent(oItem) {
    var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
    var oldValue = obj.NotifyContent;
    if (obj.NotifyContent == oItem.value) {
        return;
    }
    obj.NotifyContent = oItem.value;
    pushLog("editprop", {"obj": obj, "prop": "NotifyContent", "_old": oldValue, "_new": obj.NotifyContent});
}
function setDesc(oItem) {
    var obj = _FLOW.getStepByID(_FOCUSTEDOBJ.id);
    var oldValue = obj.Desc;
    if (obj.Desc == oItem.value) {
        return;
    }
    obj.Desc = oItem.value;
    pushLog("editprop", {"obj": obj, "prop": "Desc", "_old": oldValue, "_new": obj.Desc});
}
function setAspect(val) {
    var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
    var strRadioVal = '';
    if (Math.abs(obj.Width.replace('px', '')) < Math.abs(obj.Height.replace('px', ''))) {
        strRadioVal = 'v';
    }
    else {
        strRadioVal = 'h';
    }
    if (val == strRadioVal) {
        return;
    }
    var w = obj.Width;
    var h = obj.Height;
    var oldVal = {"X": obj.X, "Y": obj.Y, "Width": w, "Height": h};
    var d = (Math.abs(h.replace('px', '')) - Math.abs(w.replace('px', ''))) / 2;
    obj.Width = h;
    obj.Height = w;
    obj.X = Math.abs(obj.X.replace('px', '')) - d + 'px';
    obj.Y = Math.abs(obj.Y.replace('px', '')) + d + 'px';
    DrawAll();
    objFocusedOn(obj.ID);
    changeProcPos(_FOCUSTEDOBJ);
    pushLog("moveproc", {"objID": obj.ID, "moveType": "proc_nw", "_old": oldVal, "_new": {"X": obj.X, "Y": obj.Y, "Width": obj.Width, "Height": obj.Height}});
}
function stuffProp() {
    for (var i = propview.rows.length - 1;
         i > 0;
         i--) {
        propview.deleteRow(i);
    }
    if (_FOCUSTEDOBJ == null) {
        return;
    }
    var obj = _FOCUSTEDOBJ.typ == "Proc" ? _FLOW.getProcByID(_FOCUSTEDOBJ.id) : _FLOW.getStepByID(_FOCUSTEDOBJ.id);
    var idTR, idTD, oSelect, oOption;
    idTR = propview.insertRow();
    idTR.height = "22";
    idTD = idTR.insertCell();
    idTD.noWrap = true;
    idTD.innerHTML = "编 &nbsp; &nbsp;号";
    idTD.align = 'right';
    idTD = idTR.insertCell();
    idTD.innerHTML = obj.ID;


    idTR = propview.insertRow();
    idTR.height = "22";
    idTD = idTR.insertCell();
    idTD.noWrap = true;
    idTD.innerHTML = "名 &nbsp; &nbsp;称";
    idTD.align = 'right';
    idTD = idTR.insertCell();
    idTD.innerHTML = '<input type="text" rule="^\\S+$" msg="[名称]不能为空或包含空字符！" onblur="if(beforePropChange(this)) { setPropText(this);}" name="pText" maxlength="50" value="' + obj.Text + '" style="width:100%;">';
    if (_FOCUSTEDOBJ.typ == "Proc") {
        idTR = propview.insertRow();
        idTR.height = "22";
        idTD = idTR.insertCell();
        idTD.noWrap = true;
        idTD.innerHTML = "标 &nbsp; &nbsp;识";
        idTD.align = 'right';
        idTD = idTR.insertCell();
        idTD.innerHTML = '<input type="text" onblur="if(beforePropChange(this)) { setFlowNodeTag(this);}" name="FlowNodeTag" maxlength="30" value="' + obj.FlowNodeTag + '" style="width:100%;">';

        idTR = propview.insertRow();
        idTR.height = "22";
        idTD = idTR.insertCell();
        idTD.noWrap = true;
        idTD.innerHTML = "节点类型";
        idTD.align = 'right';
        idTD = idTR.insertCell();
        idTD.innerHTML = '<select name="pShapeType" onchange="setProcType(this);" style="width:100%;"></select>';
        oSelect = document.getElementById("pShapeType");
        createProcTypeOption(obj.ProcType, oSelect);
        oSelect.value = obj.ProcType;
        if (obj.ProcType == 'andjoin' || obj.PorcType == 'unandjoin') {
            idTR = propview.insertRow();
            idTR.height = "22";
            idTD = idTR.insertCell();
            idTD.noWrap = true;
            idTD.align = 'right';
            idTD.innerHTML = "&nbsp;";
            idTD = idTR.insertCell();
            idTD.innerHTML = '<input type="radio" name="forkAspect" value="v" onclick="setAspect(this.value);">纵向<input type="radio" onclick="setAspect(this.value);" value="h" name="forkAspect">横向';
            var strRadioVal = '';
            if (Math.abs(obj.Width.replace('px', '')) < Math.abs(obj.Height.replace('px', ''))) {
                strRadioVal = 'v';
            }
            else {
                strRadioVal = 'h';
            }
            var radios = document.getElementsByName('forkAspect');
            for (var i = 0;
                 i < radios.length;
                 i++) {
                if (radios[i].value == strRadioVal) {
                    radios[i].checked = true;
                }
            }
        }
        if (obj.ProcType == 'submit' || obj.ProcType == 'finish' || obj.ProcType == 'modify' || obj.ProcType == 'andsign' || obj.ProcType == 'unandsign' || obj.ProcType == 'connectsign' || obj.ProcType == 'first') {
            var strSelect = '';
            idTR = propview.insertRow();
            idTR.height = "22";
            idTD = idTR.insertCell();
            idTD.noWrap = true;
            idTD = idTR.insertCell();
            idTD.noWrap = true;
            strSelect = '';
            if (obj.SelectExit == 1) {
                strSelect = 'checked';
            }
            idTD.innerHTML = '<input type="checkbox" name="SelectExit" ' + strSelect + '  value="1" onclick="setSelectExit(this);">手动选择下一个节点处理人';

            strSelect = '';
            idTR = propview.insertRow();
            idTR.height = "22";
            idTD = idTR.insertCell();
            idTD.noWrap = true;
            idTD = idTR.insertCell();
            idTD.noWrap = true;
            strSelect = '';
            if (obj.SkipHandler == 1) {
                strSelect = 'checked';
            }
            idTD.innerHTML = '<input type="checkbox" name="SkipHandler" ' + strSelect + '  value="1" onclick="setSkipHandler(this);">跳过上次执行通过的处理人';

            strSelect = '';
            idTR = propview.insertRow();
            idTR.height = "22";
            idTD = idTR.insertCell();
            idTD.noWrap = true;
            idTD = idTR.insertCell();
            idTD.noWrap = true;
            strSelect = '';
            if (obj.AllowAgent == 1) {
                strSelect = 'checked';
            }
            idTD.innerHTML = '<input type="checkbox" name="AllowAgent" ' + strSelect + '  value="1" onclick="setAllowAgent(this);">允许代理';

            strSelect = '';
            idTR = propview.insertRow();
            idTR.height = "22";
            idTD = idTR.insertCell();
            idTD.noWrap = true;
            idTD = idTR.insertCell();
            idTD.noWrap = true;
            strSelect = '';
            if (obj.NotifyInstanceCreator == 1) {
                strSelect = 'checked';
            }
            idTD.innerHTML = '<input type="checkbox" name="NotifyInstanceCreator" ' + strSelect + '  value="1" onclick="setNotifyInstanceCreator(this);">通知流程的发起人';
            strSelect = '';
            idTR = propview.insertRow();
            idTR.height = "22";
            idTD = idTR.insertCell();
            idTD.noWrap = true;
            idTD = idTR.insertCell();
            idTD.noWrap = true;
            strSelect = '';
            if (obj.InstanceReturnMode == 1) {
                strSelect = 'checked';
            }
            idTD.innerHTML = '<input type="checkbox" name="InstanceReturnMode" ' + strSelect + '  value="1" onclick="setInstanceReturnMode(this);">并行任务处理完后再退回';


            strSelect = '';
            idTR = propview.insertRow();
            idTR.height = "22";
            idTD = idTR.insertCell();
            idTD.noWrap = true;
            idTD = idTR.insertCell();
            idTD.noWrap = true;
            strSelect = '';
            if (obj.SuspendInstance == 1) {
                strSelect = 'checked';
            }
            idTD.innerHTML = '<input type="checkbox" name="SuspendInstance" ' + strSelect + '  value="1" onclick="setSuspendInstance(this);">挂起流程';

            idTR = propview.insertRow();
            idTR.height = "22";
            idTD = idTR.insertCell();
            idTD.noWrap = true;
            idTD.align = 'right';
            idTD.innerHTML = "流程挂起描述";
            idTD = idTR.insertCell();
            idTD.innerHTML = '<input type="text" value="' + obj.SuspendDescription + '" onblur="setSuspendDescription(this);" name="SuspendDescription"  style="width:100%;">';

            idTR = propview.insertRow();
            idTR.height = "22";
            idTD = idTR.insertCell();
            idTD.noWrap = true;
            idTD.align = 'right';
            idTD.innerHTML = "取人方法";
            idTD = idTR.insertCell();
            idTD.innerHTML = '<input type="text" value="' + obj.Executer + '" onblur="setExecuter(this);" name="Executer"  style="width:100%;">';

            idTR = propview.insertRow();
            idTR.height = "22";
            idTD = idTR.insertCell();
            idTD.noWrap = true;
            idTD.align = 'right';
            idTD.innerHTML = "前面流程节点上的执行人";
            idTD = idTR.insertCell();
            idTD.innerHTML = '<input type="text" value="' + obj.HandlerOnFlowNode + '" onblur="setHandlerOnFlowNode(this);" name="HandlerOnFlowNode"  style="width:100%;">';

            idTR = propview.insertRow();
            idTR.height = "22";
            idTD = idTR.insertCell();
            idTD.noWrap = true;
            idTD.align = 'right';
            idTD.innerHTML = "实体上的用户属性";
            idTD = idTR.insertCell();
            idTD.innerHTML = '<input type="text" value="' + obj.UserPropertyOnEntity + '" onblur="setUserPropertyOnEntity(this);" name="UserPropertyOnEntity"  style="width:100%;">';

            if (obj.ProcType != 'first') {
                /*
                 idTR = propview.insertRow();
                 idTR.height = "22";
                 idTD = idTR.insertCell();
                 idTD.align = 'right';
                 idTD.noWrap = true;
                 idTD.innerHTML = "协助人(待阅)";
                 idTD = idTR.insertCell();
                 idTD.innerHTML = '<input type="text" value="' + obj.Aidancer + '" onblur="setAidancer(this);" name="Aidancer"  style="width:100%;">';
                 */
                idTR = propview.insertRow();
                idTR.height = "22";
                idTD = idTR.insertCell();
                idTD.align = 'right';
                idTD.noWrap = true;
                idTD.innerHTML = "组织机构ID";
                idTD = idTR.insertCell();
                idTD.innerHTML = '<input type="text" value="' + obj.OrganizationId + '" onblur="setOrganizationId(this);" name="OrganizationId"  style="width:100%;">';
                idTR = propview.insertRow();
                idTR.height = "22";
                idTD = idTR.insertCell();
                idTD.align = 'right';
                idTD.noWrap = true;
                idTD.innerHTML = "角色ID";
                idTD = idTR.insertCell();
                idTD.innerHTML = '<input type="text" value="' + obj.RoleId + '" onblur="setRoleId(this);" name="RoleId"  style="width:100%;">';

                idTR = propview.insertRow();
                idTR.height = "22";
                idTD = idTR.insertCell();
                idTD.align = 'right';
                idTD.noWrap = true;
                idTD.innerHTML = "使用根节点";
                idTD = idTR.insertCell();
                idTD.innerHTML = '<input type="text" value="' + obj.UseRootNode + '" onblur="setUseRootNode(this);" name="UseRootNode"  style="width:100%;">';
                idTR = propview.insertRow();
                idTR.height = "22";
                idTD = idTR.insertCell();
                idTD.align = 'right';
                idTD.noWrap = true;
                idTD.innerHTML = "组织机构属性";
                idTD = idTR.insertCell();
                idTD.innerHTML = '<input type="text" value="' + obj.OrganizationProperty + '" onblur="setOrganizationProperty(this);" name="OrganizationProperty"  style="width:100%;">';
                idTR = propview.insertRow();
                idTR.height = "22";
                idTD = idTR.insertCell();
                idTD.align = 'right';
                idTD.noWrap = true;
                idTD.innerHTML = "角色属性";
                idTD = idTR.insertCell();
                idTD.innerHTML = '<input type="text" value="' + obj.RoleProperty + '" onblur="setRoleProperty(this);" name="RoleProperty"  style="width:100%;">';
                idTR = propview.insertRow();
                idTR.height = "22";
                idTD = idTR.insertCell();
                idTD.align = 'right';
                idTD.noWrap = true;
                idTD.innerHTML = "取人数";
                idTD = idTR.insertCell();
                idTD.innerHTML = '<input type="text" value="' + obj.HandlerFetchCount + '" onblur="setHandlerFetchCount(this);" name="HandlerFetchCount"  style="width:100%;">';

                idTR = propview.insertRow();
                idTR.height = "22";
                idTD = idTR.insertCell();
                idTD.align = 'right';
                idTD.noWrap = true;
                idTD.innerHTML = "取人方式";
                idTD = idTR.insertCell();
                strSelect = '';
                if (obj.HandlerFetchType && obj.HandlerFetchType.indexOf('Random') != -1) {
                    strSelect = 'checked';
                }
                idTD.innerHTML = '<input type="checkbox" onclick="setHandlerFetchType();" ' + strSelect + ' name="Random" value="Random" >随机';
                strSelect = '';
                if (obj.HandlerFetchType && obj.HandlerFetchType.indexOf('IdleMost') != -1) {
                    strSelect = 'checked';
                }
                idTD.innerHTML += '<input value="IdleMost" onclick="setHandlerFetchType();" ' + strSelect + ' type="checkbox" name="IdleMost" >最空闲';
                strSelect = '';
                if (obj.HandlerFetchType && obj.HandlerFetchType.indexOf('TaskLeast') != -1) {
                    strSelect = 'checked';
                }
                idTD.innerHTML += '<br><input type="checkbox" onclick="setHandlerFetchType();" ' + strSelect + ' name="TaskLeast"  value="TaskLeast">同类任务最少';

                idTR = propview.insertRow();
                idTR.height = "22";
                idTD = idTR.insertCell();
                idTD.align = 'right';
                idTD.noWrap = true;
                idTD.innerHTML = "处理时限";
                idTD = idTR.insertCell();
                idTD.innerHTML = '<input type="text" name="TimeLimit" value="' + obj.TimeLimit + '" onblur="setTimeLimit(this);"  style="width:40px;">工作小时';

                idTR = propview.insertRow();
                idTR.height = "22";
                idTD = idTR.insertCell();
                idTD.align = 'right';
                idTD.noWrap = true;
                idTD.innerHTML = "行为动作";
                idTD = idTR.insertCell();
                strSelect = '';

                if (obj.FlowNodeAction && obj.FlowNodeAction.indexOf('passAction') != -1) {
                    strSelect = 'checked';
                }
                idTD.innerHTML = '<input type="checkbox" onclick="setFlowNodeAction();" ' + strSelect + ' name="passAction" value="passAction" >通过';
                strSelect = '';
                if (obj.FlowNodeAction && obj.FlowNodeAction.indexOf('returnAction') != -1) {
                    strSelect = 'checked';
                }
                idTD.innerHTML += '<input value="returnAction" onclick="setFlowNodeAction();" ' + strSelect + ' type="checkbox" name="returnAction" >退回';
                strSelect = '';
                if (obj.FlowNodeAction && obj.FlowNodeAction.indexOf('rejectAction') != -1) {
                    strSelect = 'checked';
                }
                idTD.innerHTML += '<br><input type="checkbox" onclick="setFlowNodeAction();" ' + strSelect + ' name="rejectAction"  value="rejectAction">否决';
                strSelect = '';
                if (obj.FlowNodeAction && obj.FlowNodeAction.indexOf('completeAction') != -1) {
                    strSelect = 'checked';
                }
                idTD.innerHTML += '<br><input type="checkbox" onclick="setFlowNodeAction();" ' + strSelect + ' name="completeAction"  value="completeAction">完成';
                strSelect = '';
                if (obj.FlowNodeAction && obj.FlowNodeAction.indexOf('discardAction') != -1) {
                    strSelect = 'checked';
                }
                idTD.innerHTML += '<br><input type="checkbox" onclick="setFlowNodeAction();" ' + strSelect + ' name="discardAction"  value="discardAction">弃权';

                idTR = propview.insertRow();
                idTR.height = "22";
                idTD = idTR.insertCell();
                idTD.align = 'right';
                idTD.noWrap = true;
                idTD.innerHTML = "提醒方式";
                idTD = idTR.insertCell();
                strSelect = '';
                if (obj.WarnType && obj.WarnType.indexOf('note') != -1) {
                    strSelect = 'checked';
                }
                idTD.innerHTML = '<input type="checkbox" onclick="setWarnType();" ' + strSelect + ' name="note" value="note" >内部消息';
                strSelect = '';
                if (obj.WarnType && obj.WarnType.indexOf('sms') != -1) {
                    strSelect = 'checked';
                }
                idTD.innerHTML += '<input value="sms" onclick="setWarnType();" ' + strSelect + ' type="checkbox" name="sms" >手机短信';
                strSelect = '';
                if (obj.WarnType && obj.WarnType.indexOf('email') != -1) {
                    strSelect = 'checked';
                }
                idTD.innerHTML += '<br><input type="checkbox" onclick="setWarnType();" ' + strSelect + ' name="email"  value="email">邮件';

                idTR = propview.insertRow();
                idTR.height = "22";
                idTD = idTR.insertCell();
                idTD.align = 'right';
                idTD.noWrap = true;
                idTD.innerHTML = "表单状态";
                idTD = idTR.insertCell();
                idTD.innerHTML = '<input type="text" value="' + obj.EntityStatusTag + '" onblur="setEntityStatusTag(this);" name="EntityStatusTag"  style="width:100%;">';

                idTR = propview.insertRow();
                idTR.height = "22";
                idTD = idTR.insertCell();
                idTD.align = 'right';
                idTD.noWrap = true;
                idTD.innerHTML = "处理界面";
                idTD = idTR.insertCell();
                idTD.innerHTML = '<input type="text" value="' + obj.Activity + '" onblur="setActivity(this);" name="Activity"  style="width:100%;">';

                idTR = propview.insertRow();
                idTR.height = "22";
                idTD = idTR.insertCell();
                idTD.align = 'right';
                idTD.noWrap = true;
                idTD.innerHTML = "通知内容模板";
                idTD = idTR.insertCell();
                idTD.innerHTML = '<textarea rows="6" onblur="setNotifyContent(this);" name="NotifyContent"  style="width:100%;" >' + obj.NotifyContent + '</textarea>';
            }
        }
    }
    else {
        idTR = propview.insertRow();
        idTR.height = "22";
        idTD = idTR.insertCell();
        idTD.align = 'right';
        idTD.noWrap = true;
        idTD.innerHTML = "分支标识";
        idTD = idTR.insertCell();
        idTD.innerHTML = '<input type="text" onblur="setRamusTag(this);" value="' + obj.RamusTag + '" name="RamusTag"  style="width:100%;">';
        idTR = propview.insertRow();
        idTR.height = "22";
        idTD = idTR.insertCell();
        idTD.align = 'right';
        idTD.noWrap = true;
        idTD.innerHTML = "状态标识";
        idTD = idTR.insertCell();
        idTD.innerHTML = '<input type="text" onblur="setStatusTag(this);" value="' + obj.StatusTag + '" name="StatusTag"  style="width:100%;">';

        idTR = propview.insertRow();
        idTR.height = "22";
        idTD = idTR.insertCell();
        idTD.align = 'right';
        idTD.noWrap = true;
        idTD.innerHTML = "条 &nbsp; &nbsp;件";
        idTD = idTR.insertCell();
        //idTD.innerHTML = '<input type="text" onblur="setCond(this);" value="' + obj.Cond + '" name="Cond"  style="width:100%;">';
        idTD.innerHTML = '<textarea rows="6" onblur="setCond(this);" name="Cond"  style="width:100%;" >' + obj.Cond + '</textarea>';
        idTR = propview.insertRow();
        idTR.height = "22";
        idTD = idTR.insertCell();
        idTD.align = 'right';
        idTD.noWrap = true;
        idTD.innerHTML = "事 &nbsp; &nbsp;件";
        idTD = idTR.insertCell();
        idTD.innerHTML = '<input type="text" onblur="setEvent(this);" value="' + obj.Event + '" name="Event"  style="width:100%;">';
        idTR = propview.insertRow();
        idTR.height = "22";
        idTD = idTR.insertCell();
        idTD.align = 'right';
        idTD.noWrap = true;
        idTD.innerHTML = "字段权限";
        idTD = idTR.insertCell();
        idTD.innerHTML = '';
        idTR = propview.insertRow();
        idTR.height = "22";
        idTD = idTR.insertCell();
        idTD.align = 'right';
        idTD.noWrap = true;
        idTD.innerHTML = "说 &nbsp; &nbsp;明";
        idTD = idTR.insertCell();
        idTD.innerHTML = '<textarea name="Desc" onblur="setDesc(this);" style="width:100%;height:45px;">' + obj.Desc + '</textarea>';
    }
}
function createProcTypeOption(ptype, oSelect) {
    switch (ptype) {
        case'begin':
            oOption = document.createElement("OPTION");
            oSelect.options.add(oOption);
            oOption.innerText = "开始";
            oOption.value = "begin";
            break;
        case'end':
            oOption = document.createElement("OPTION");
            oSelect.options.add(oOption);
            oOption.innerText = "结束";
            oOption.value = "end";
            break;
        case'first':
            oOption = document.createElement("OPTION");
            oSelect.options.add(oOption);
            oOption.innerText = "第一个节点";
            oOption.value = "first";
            break;
        case'andsign':
        case'unandsign':
        case'connectsign':
        case'fork':
        case'andjoin':
        case'unandjoin':
        default:
            oOption = document.createElement("OPTION");
            oSelect.options.add(oOption);
            oOption.innerText = "会签";
            oOption.value = "andsign";
            oOption = document.createElement("OPTION");
            oSelect.options.add(oOption);
            oOption.innerText = "修改";
            oOption.value = "modify";

            oOption = document.createElement("OPTION");
            oSelect.options.add(oOption);
            oOption.innerText = "提交";
            oOption.value = "submit";

            oOption = document.createElement("OPTION");
            oSelect.options.add(oOption);
            oOption.innerText = "完成";
            oOption.value = "finish";

            /*oOption = document.createElement("OPTION");
             oSelect.options.add(oOption);
             oOption.innerText = "非会签";
             oOption.value = "unandsign";
             oOption = document.createElement("OPTION");
             oSelect.options.add(oOption);
             oOption.innerText = "串签";
             oOption.value = "connectsign";
             oOption = document.createElement("OPTION");
             oSelect.options.add(oOption);
             oOption.innerText = "分支节点";
             oOption.value = "fork";
             oOption = document.createElement("OPTION");
             oSelect.options.add(oOption);
             oOption.innerText = "并流会聚";
             oOption.value = "andjoin";
             oOption = document.createElement("OPTION");
             oSelect.options.add(oOption);
             oOption.innerText = "非并流会聚";
             oOption.value = "unandjoin";*/
            break;
    }
}
function emptyLog() {
    _DOLOG = [];
    _DOLOGINDEX = -1;
}
function pushLog(act, obj) {
    var newLog = _DOLOG.slice(0, _DOLOGINDEX + 1);
    _DOLOG = newLog;
    _DOLOGINDEX = _DOLOG.push({"act": act, "val": obj}) - 1;
}
function getLog() {
    return _DOLOG[_DOLOGINDEX];
}
function undoLog() {
    if (_DOLOGINDEX == -1) {
        alert("没有操作记录可以撤消.");
        return;
    }
    if (doLog("undo")) {
        _DOLOGINDEX--;
    }
}
function redoLog() {
    if (_DOLOGINDEX == _DOLOG.length - 1) {
        alert("没有操作记录可以恢复.");
        return;
    }
    _DOLOGINDEX++;
    doLog("redo");
}
function doLog(act) {
    var log = getLog();
    switch (log.act) {
        case"addproc":
            act == "undo" ? _FLOW.deleteProcByID(log.val.ID) : _FLOW.addProc(log.val);
            DrawAll();
            break;
        case"addstep":
            act == "undo" ? _FLOW.deleteStepByID(log.val.ID) : _FLOW.addStep(log.val);
            DrawAll();
            break;
        case"delproc":
            act == "undo" ? _FLOW.addProc(log.val) : _FLOW.deleteProcByID(log.val.ID);
            DrawAll();
            break;
        case"delstep":
            act == "undo" ? _FLOW.addStep(log.val) : _FLOW.deleteStepByID(log.val.ID);
            DrawAll();
            break;
        case"editproc":
            if (act == "undo") {
                var Proc = _FLOW.getProcByID(log.val._new.ID);
                Proc.clone(log.val._old);
                if (log.val._new.ID != log.val._old.ID) {
                    changeProcID(log.val._new.ID, log.val._old.ID);
                }
            }
            else {
                var Proc = _FLOW.getProcByID(log.val._old.ID);
                Proc.clone(log.val._new);
                if (log.val._new.ID != log.val._old.ID) {
                    changeProcID(log.val._old.ID, log.val._new.ID);
                }
            }
            DrawAll();
            objFocusedOn(act == "undo" ? log.val._old.ID : log.val._new.ID);
            break;
        case"editstep":
            if (act == "undo") {
                var Step = _FLOW.getStepByID(log.val._new.ID);
                Step.clone(log.val._old);
            }
            else {
                var Step = _FLOW.getStepByID(log.val._old.ID);
                Step.clone(log.val._new);
            }
            DrawVML();
            objFocusedOn(act == "undo" ? log.val._old.ID : log.val._new.ID);
            break;
        case"moveproc":
            var obj = _FLOW.getProcByID(log.val.objID);
            if (act == "undo") {
                obj.setPropValue("X", log.val._old.X);
                obj.setPropValue("Y", log.val._old.Y);
                obj.setPropValue("Width", log.val._old.Width);
                obj.setPropValue("Height", log.val._old.Height);
            }
            else {
                obj.setPropValue("X", log.val._new.X);
                obj.setPropValue("Y", log.val._new.Y);
                obj.setPropValue("Width", log.val._new.Width);
                obj.setPropValue("Height", log.val._new.Height);
            }
            changeProcPos(obj.InnerObject);
            DrawAll();
            break;
        case"editprop":
            var CurrentProp = (act == "undo" ? log.val._old : log.val._new);
            if (log.val.obj[log.val.prop]) {
                log.val.obj[log.val.prop] = CurrentProp;
            }
            switch (log.val.prop) {
                case"ID":
                    if (log.val.obj.ObjType == "Proc") {
                        act == "undo" ? changeProcID(log.val._new, log.val._old) : changeProcID(log.val._old, log.val._new);
                    }
                    DrawVML();
                    objFocusedOn(log.val.obj.ID);
                    break;
                case"Text":
                    DrawAll();
                    objFocusedOn(log.val.obj.ID);
                    break;
                case"ProcType":
                    var _shapeType = (act == "undo" ? log.val._oldShape : log.val._newShape);
                    log.val.obj.ShapeType = _shapeType;
                    DrawVML();
                    objFocusedOn(log.val.obj.ID);
                    break;
                case"TextWeight":
                    document.all(log.val.obj.ID + "Text").style.fontSize = CurrentProp;
                    break;
                case"zIndex":
                    log.val.obj.InnerObject.style.zIndex = CurrentProp;
                    break;
                case"StrokeWeight":
                    log.val.obj.InnerObject.strokeweight = CurrentProp;
                    break;
                case"X":
                    log.val.obj.InnerObject.style.left = CurrentProp;
                    changeProcPos(log.val.obj.InnerObject);
                    break;
                case"Y":
                    log.val.obj.InnerObject.style.top = CurrentProp;
                    changeProcPos(log.val.obj.InnerObject);
                    break;
                case"Width":
                    log.val.obj.InnerObject.style.width = CurrentProp;
                    changeProcPos(log.val.obj.InnerObject);
                    break;
                case"Height":
                    log.val.obj.InnerObject.style.height = CurrentProp;
                    changeProcPos(log.val.obj.InnerObject);
                    break;
                case"Cond":
                    document.all(log.val.obj.ID + "Text").innerHTML = CurrentProp;
                    break;
                case"StartArrow":
                    document.all(log.val.obj.ID + "Arrow").startarrow = CurrentProp;
                    break;
                case"EndArrow":
                    document.all(log.val.obj.ID + "Arrow").endarrow = CurrentProp;
                    break;
                case"FromProc":
                case"ToProc":
                    if (log.val.obj.ShapeType == "Line") {
                        log.val.obj.getPath();
                        log.val.obj.InnerObject.from = log.val.obj.FromPoint;
                        log.val.obj.InnerObject.to = log.val.obj.ToPoint;
                    }
                    else {
                        if (log.val.ShapeType == "PolyLine") {
                            log.val.obj.InnerObject.points.value = log.val.obj.getPath();
                        }
                    }
                    DrawDataView();
                    break;
            }
            break;
    }
    stuffProp();
    return true;
}
function doProcMouseDown(obj, x, y) {
    if (_TOOLTYPE == "line" || _TOOLTYPE == "polyline") {
        _CURRENTX = x;
        _CURRENTY = y;
        _MOVEOBJ = document.all("_lineui");
        _MOVEOBJ.from = _CURRENTX + "," + (_CURRENTY - 0);
        _MOVEOBJ.to = _MOVEOBJ.from;
        _MOVEOBJ.style.display = "block";
        _MOVETYPE = _TOOLTYPE;
    }
    else {
        var rightSide = (parseInt(obj.style.left) + parseInt(obj.style.width) - x <= 2);
        var bottomSide = (parseInt(obj.style.top) + parseInt(obj.style.height) - y <= 2);
        if (rightSide && bottomSide) {
            _MOVETYPE = "proc_nw";
        }
        else {
            if (rightSide) {
                _MOVETYPE = "proc_e";
            }
            else {
                if (bottomSide) {
                    _MOVETYPE = "proc_n";
                }
                else {
                    _MOVETYPE = "proc_m";
                    _CURRENTX = x - obj.offsetLeft;
                    _CURRENTY = y - obj.offsetTop;
                }
            }
        }
        _MOVEOBJ = obj;
    }
    window.event.cancelBubble = true;
}
function fireProcMouseDown(x, y) {
    var curProc = null;
    for (var i = 0;
         i < _FLOW.Procs.length;
         i++) {
        Proc = _FLOW.Procs[i];
        if (x >= parseInt(Proc.X) && x <= (parseInt(Proc.X) + parseInt(Proc.Width)) && y >= parseInt(Proc.Y) && y <= (parseInt(Proc.Y) + parseInt(Proc.Height))) {
            curProc = Proc;
        }
    }
    for (var i = 0;
         i < _FLOW.Steps.length;
         i++) {
        Proc = _FLOW.Steps[i].Label;
        if (x >= parseInt(Proc.X) && x <= (parseInt(Proc.X) + parseInt(Proc.Width)) && y >= parseInt(Proc.Y) && y <= (parseInt(Proc.Y) + parseInt(Proc.Height))) {
            curProc = Proc;
        }
    }
    if (curProc != null) {
        obj = document.getElementById(curProc.ID);
        if (obj.tagName != 'DIV') {
            objFocusedOn(obj.id);
        }
        doProcMouseDown(obj, x, y);
        return true;
    }
    return false;
}
function doProcMouseMove(obj, x, y) {
    if (_TOOLTYPE == "line" || _TOOLTYPE == "polyline") {
        document.all.Canvas.style.cursor = "crosshair";
    }
    else {
        var rightSide = (parseInt(obj.style.left) + parseInt(obj.style.width) - x <= 2);
        var bottomSide = (parseInt(obj.style.top) + parseInt(obj.style.height) - y <= 2);
        if (rightSide && bottomSide) {
            document.all.Canvas.style.cursor = "NW-resize";
        }
        else {
            if (rightSide) {
                document.all.Canvas.style.cursor = "E-resize";
            }
            else {
                if (bottomSide) {
                    document.all.Canvas.style.cursor = "N-resize";
                }
                else {
                    document.all.Canvas.style.cursor = "hand";
                }
            }
        }
    }
}
function fireProcMouseMove(x, y) {
    if (document.all.Canvas == null) {
        return;
    }
    for (var i = 0;
         i < _FLOW.Procs.length;
         i++) {
        Proc = _FLOW.Procs[i];
        obj = document.getElementById(Proc.ID);
        if (x >= parseInt(Proc.X) && x <= (parseInt(Proc.X) + parseInt(Proc.Width)) && y >= parseInt(Proc.Y) && y <= (parseInt(Proc.Y) + parseInt(Proc.Height))) {
            doProcMouseMove(obj, x, y);
            return true;
        }
    }
    for (var i = 0;
         i < _FLOW.Steps.length;
         i++) {
        Proc = _FLOW.Steps[i].Label;
        obj = document.getElementById(Proc.ID);
        if (x >= parseInt(Proc.X) && x <= (parseInt(Proc.X) + parseInt(Proc.Width)) && y >= parseInt(Proc.Y) && y <= (parseInt(Proc.Y) + parseInt(Proc.Height))) {
            doProcMouseMove(obj, x, y);
            return true;
        }
    }
    if (i >= _FLOW.Procs.length) {
        document.all.Canvas.style.cursor = (_TOOLTYPE == "point" ? "default" : "crosshair");
    }
    if (i >= _FLOW.Labels.length) {
        document.all.Canvas.style.cursor = (_TOOLTYPE == "point" ? "default" : "crosshair");
    }
    return false;
}
function doDocMouseDown() {
    if (event.button == 2) {
        return false;
    }
    var x = (event.x + document.body.scrollLeft) / _ZOOM;
    var y = (event.y + document.body.scrollTop) / _ZOOM;
    var oEvt = event.srcElement;
    if (oEvt.id == "tableContainer" || oEvt.id == "") {
        return;
    }
    if (oEvt.typ == "Step") {
        document.all.Canvas.style.cursor = "default";
        return;
    }
    if (fireProcMouseDown(x, y)) {
        return;
    }
    switch (_TOOLTYPE) {
        case"rect":
        case"roundrect":
        case"diamond":
        case"oval":
        case"fillrect":
            if (oEvt.tagName != "DIV") {
                return;
            }
            if (oEvt.id != "Canvas") {
                return;
            }
            var obj = document.all("_" + _TOOLTYPE + "ui");
            _CURRENTX = x;
            _CURRENTY = y;
            obj.style.left = _CURRENTX;
            obj.style.top = _CURRENTY;
            obj.style.width = 0;
            obj.style.height = 0;
            obj.style.display = "block"
            _MOVETYPE = _TOOLTYPE;
            break;
    }
}
function doDocMouseMove() {
    var x = (event.x + document.body.scrollLeft) / _ZOOM;
    var y = (event.y + document.body.scrollTop) / _ZOOM;
    var m, n, aryPt, _movePt, sqrta, _moveLine
    switch (_MOVETYPE) {
        case"line":
        case"polyline":
            _MOVEOBJ.to = x + "," + (y - 0);
            break;
        case"line_m":
            var zx = x * _ZOOM
            var zy = y * _ZOOM
            if (oOval == null) {
                createOval(x * _ZOOM, y * _ZOOM);
                if (_PointOrLine == 0) {
                    oOval.fillcolor = "blue";
                    oOval.strokecolor = "blue";
                }
            }
            m = _clkPx.substr(0, _clkPx.length - 2) * 4 / 3;
            n = _clkPy.substr(0, _clkPy.length - 2) * 4 / 3;
            sqrta = Math.sqrt((x - m) * (x - m) + (y - n) * (y - n));
            var _arySltLine = _strSltLine.split(',');
            if (sqrta > 10) {
                if (oOval) {
                    oOval.style.left = zx - 3;
                    oOval.style.top = zy - 3;
                }
                if (_PointOrLine == 0) {
                    _movePt = (x * 3 / 4) + 'pt,' + (y * 3 / 4) + "pt";
                    _MOVEOBJ.Points = _strPt1 + "" + _movePt + "" + _strPt2;
                }
                else {
                    _moveLine = _arySltLine[0] + ',' + _arySltLine[1] + ',' + x * 3 / 4 + 'pt,' + y * 3 / 4 + 'pt,' + _arySltLine[2] + ',' + _arySltLine[3];
                    _MOVEOBJ.Points = _strLine1 + _moveLine + _strLine2;
                }
                document.getElementById(_MOVEOBJ.ID).outerHTML = _MOVEOBJ.toStringWithoutLabel();
                _FLOW.Modified = true;
            }
            break;
        case"proc_m":
            m = _clkPx.substr(0, _clkPx.length - 2) * 4 / 3;
            n = _clkPy.substr(0, _clkPy.length - 2) * 4 / 3;
            sqrta = Math.sqrt((x - m) * (x - m) + (y - n) * (y - n));
            if (_MOVEOBJ.tagName == 'DIV') {
                var newX = x - _CURRENTX;
                var newY = y - _CURRENTY;
                _MOVEOBJ.style.left = newX + 2;
                _MOVEOBJ.style.top = newY + 2;
            }
            else {
                if (sqrta > 10) {
                    var newX = x - _CURRENTX;
                    var newY = y - _CURRENTY;
                    if (newX < 0) {
                        newX = 0;
                    }
                    if (newY < 30) {
                        newY = 30;
                    }
                    _MOVEOBJ.style.left = getMinMod(newX, 10);
                    _MOVEOBJ.style.top = getMinMod(newY, 10);
                    if (_MOVEOBJ.tagName != 'DIV') {
                        changeProcPos(_MOVEOBJ);
                    }
                }
            }
            break;
        case"proc_n":
            var newH = y - parseInt(_MOVEOBJ.style.top);
            if (newH < 30) {
                newH = 30;
            }
            _MOVEOBJ.style.height = getMinMod(newH, 10);
            if (_MOVEOBJ.tagName != 'DIV') {
                changeProcPos(_MOVEOBJ);
            }
            break;
        case"proc_e":
            var newW = x - parseInt(_MOVEOBJ.style.left);
            if (newW < 35) {
                newW = 35;
            }
            _MOVEOBJ.style.width = getMinMod(newW, 10);
            if (_MOVEOBJ.tagName != 'DIV') {
                changeProcPos(_MOVEOBJ);
            }
            break;
        case"proc_nw":
            var newW = x - parseInt(_MOVEOBJ.style.left);
            var newH = y - parseInt(_MOVEOBJ.style.top);
            if (newW < 30) {
                newW = 30;
            }
            if (newH < 30) {
                newH = 30;
            }
            _MOVEOBJ.style.width = getMinMod(newW, 10);
            _MOVEOBJ.style.height = getMinMod(newH, 10);
            if (_MOVEOBJ.tagName != 'DIV') {
                changeProcPos(_MOVEOBJ);
            }
            break;
        case"rect":
        case"roundrect":
        case"diamond":
        case"oval":
        case"fillrect":
            var newX = x;
            var newY = y;
            var obj = document.all("_" + _MOVETYPE + "ui");
            if (newX < _CURRENTX) {
                obj.style.left = newX;
            }
            obj.style.width = Math.abs(newX - _CURRENTX);
            if (newY < _CURRENTY) {
                obj.style.top = newY;
            }
            obj.style.height = Math.abs(newY - _CURRENTY);
            break;
        default:
            fireProcMouseMove(x, y);
    }
}
function doDocMouseUp() {
    if (event.button == 2) {
        return false;
    }
    var x = (event.x + document.body.scrollLeft) / _ZOOM;
    var y = (event.y + document.body.scrollTop) / _ZOOM;
    if (oOval) {
        document.body.removeChild(oOval);
    }
    oOval = null;
    switch (_MOVETYPE) {
        case"line_m":
            var ProcTo = "";
            ProcTo = _FLOW.getProcAtXY(x, y);
            if (ProcTo == null) {
                if (ptMoveType == "from" || ptMoveType == "to") {
                    _MOVEOBJ.Points = _strPt1 + _strSltPt + _strPt2;
                    document.getElementById(_MOVEOBJ.ID).outerHTML = _MOVEOBJ.toStringWithoutLabel();
                }
            }
            else {
                if (ptMoveType == "from" || ptMoveType == "to") {
                    var Proc1, Proc2;
                    Proc1 = _FLOW.getProcAtXY(_CURRENTX, _CURRENTY);
                    Proc2 = _FLOW.getProcAtXY(x, y);
                    var nearPt = getNearPt(ProcTo, x, y);
                    var strPt = nearPt.split("|~|")[0];
                    var arrPt = strPt.split(",");
                    var strPos = nearPt.split("|~|")[1];
                    var nX = arrPt[0].substr(0, arrPt[0].length - 2);
                    var nY = arrPt[1].substr(0, arrPt[1].length - 2);
                    var relX = strPos.split(",")[0];
                    var relY = strPos.split(",")[1];
                    if (ptMoveType == "from") {
                        Proc1 = ProcTo;
                        Proc2 = _FLOW.getProcByID(_MOVEOBJ.ToProc);
                    }
                    if (ptMoveType == "to") {
                        Proc1 = _FLOW.getProcByID(_MOVEOBJ.FromProc);
                        Proc2 = ProcTo;
                    }
                    var existProc = _FLOW.StepPathExists(Proc1.ID, Proc2.ID)
                    if ((Proc1.ID == Proc2.ID) || (existProc != null && existProc.ID != _MOVEOBJ.ID) || (Proc1.ProcType == "end" || Proc2.ProcType == "begin") || (_MOVEOBJ.FromProc == "begin" && ptMoveType == "from" && ProcTo.ID != _MOVEOBJ.FromProc) || (_MOVEOBJ.FromProc == "begin" && ptMoveType == "to" && ProcTo.ID != _MOVEOBJ.ToProc) || (_MOVEOBJ.FromProc != "begin" && ProcTo.ID == "begin")) {
                        if (existProc != null && existProc.ID != _MOVEOBJ.ID) {
                            alert("已经有一个路径从[" + _FLOW.getProcByID(Proc1.ID).Text + "]至[" + _FLOW.getProcByID(Proc2.ID).Text + "]，操作不成功");
                        }
                        if (Proc1.ProcType == "end" || Proc2.ProcType == "begin") {
                            alert("路径必须符合“起点不能是结束结点，终点不能是开始结点”的规则！");
                        }
                        if (Proc1.ID == Proc2.ID) {
                            alert("不能指向本身！");
                        }
                        _MOVEOBJ.Points = _strPt1 + _strSltPt + _strPt2;
                        document.getElementById(_MOVEOBJ.ID).outerHTML = _MOVEOBJ.toStringWithoutLabel();
                    }
                    else {
                        if (ptMoveType == "from") {
                            _MOVEOBJ.setPropValue("FromProc", ProcTo.ID);
                            _MOVEOBJ.fromRelX = relX;
                            _MOVEOBJ.fromRelY = relY;
                        }
                        if (ptMoveType == "to") {
                            _MOVEOBJ.setPropValue("ToProc", ProcTo.ID);
                            _MOVEOBJ.toRelX = relX;
                            _MOVEOBJ.toRelY = relY;
                        }
                        _MOVEOBJ.Points = _strPt1 + strPt + _strPt2;
                        document.getElementById(_MOVEOBJ.ID).outerHTML = _MOVEOBJ.toStringWithoutLabel();
                        _FLOW.Modified = true;
                    }
                }
            }
            var newobj = new TStep(_FLOW);
            newobj.clone(_MOVEOBJ)
            pushLog("editstep", {"_old": _MOVELINEOBJ, "_new": newobj});
            if (oOval) {
                document.body.removeChild(oOval);
            }
            oOval = null;
            document.getElementById(_MOVEOBJ.ID).StrokeColor = document.getElementById(_MOVEOBJ.ID).fsc;
            _FOCUSTEDOBJ = document.getElementById(_MOVEOBJ.ID);
            document.getElementById(_MOVEOBJ.Label.ID).style.backgroundColor = '#dddddd';
            break;
        case"proc_m":
        case"proc_n":
        case"proc_e":
        case"proc_nw":
            if (_MOVEOBJ.tagName != 'DIV') {
                var Proc = _FLOW.getProcByID(_MOVEOBJ.id);
                var oldVal = {"X": Proc.X, "Y": Proc.Y, "Width": Proc.Width, "Height": Proc.Height};
                if (_MOVETYPE == "proc_m") {
                    Proc.setPropValue("X", _MOVEOBJ.style.left);
                    Proc.setPropValue("Y", _MOVEOBJ.style.top);
                }
                else {
                    Proc.setPropValue("Width", _MOVEOBJ.style.width);
                    Proc.setPropValue("Height", _MOVEOBJ.style.height);
                }
                if (Math.abs(parseInt(oldVal.X) - parseInt(Proc.X)) > 2 || Math.abs(parseInt(oldVal.Y) - parseInt(Proc.Y)) > 2 || Math.abs(parseInt(oldVal.Width) - parseInt(Proc.Width)) > 2 || Math.abs(parseInt(oldVal.Height) - parseInt(Proc.Height)) > 2) {
                    pushLog("moveproc", {"objID": Proc.ID, "moveType": _MOVETYPE, "_old": oldVal, "_new": {"X": Proc.X, "Y": Proc.Y, "Width": Proc.Width, "Height": Proc.Height}});
                }
                stuffProp();
                _FLOW.Modified = true;
                saveStepsToProc(_MOVEOBJ);
            }
            else {
                var stepid = _MOVEOBJ.id.replace('lab', '');
                var Label = _FLOW.getStepByID(stepid).Label;
                if (_MOVETYPE == "proc_m") {
                    Label.X = _MOVEOBJ.style.left;
                    Label.Y = _MOVEOBJ.style.top;
                }
                else {
                    Label.Width = _MOVEOBJ.style.width;
                    Label.Height = _MOVEOBJ.style.height;
                }
            }
            if (oOval) {
                document.body.removeChild(oOval);
            }
            oOval = null;
            break;
        case"rect":
        case"roundrect":
        case"diamond":
        case"oval":
        case"fillrect":
        case"line":
        case"polyline":
            var obj = document.all("_" + (_MOVETYPE == "polyline" ? "line" : _MOVETYPE) + "ui");
            obj.style.display = "none";
            if (_MOVETYPE == "line" || _MOVETYPE == "polyline") {
                var Proc1, Proc2, Step;
                Proc1 = _FLOW.getProcAtXY(_CURRENTX, _CURRENTY);
                Proc2 = _FLOW.getProcAtXY(x, y);
                if (Proc1 == null || Proc2 == null) {
                    alert("起点或终点不是[任务]，请在[任务]图形上按住鼠标并拖动到某[任务]图形上松开.");
                    break;
                }
                if (_FLOW.StepPathExists(Proc1.ID, Proc2.ID) != null) {
                    alert("已经有一个路径从[" + _FLOW.getProcByID(Proc1.ID).Text + "]至[" + _FLOW.getProcByID(Proc2.ID).Text + "]，请更改！");
                    break;
                }
                if (Proc1.ID == Proc2.ID) {
                    alert("不能指向本身！");
                    break;
                }
                if (Proc1.ProcType == "begin") {
                    alert("开始节点不允许手动联线!");
                    break;
                }
                if (Proc1.ProcType == "end" || Proc2.ProcType == "begin") {
                    alert("路径必须符合“起点不能是结束结点，终点不能是开始结点”的规则！");
                    break;
                }
                Step = new TStep(_FLOW);
                Step.FromProc = Proc1.ID;
                Step.ToProc = Proc2.ID;
                Step.zIndex = 2;
                Step.ShapeType = "PolyLine";
                Step.Label.X = (_CURRENTX + x) / 2;
                Step.Label.Y = (_CURRENTY + y) / 2
                _FLOW.addStep(Step);
                pushLog("addstep", Step);
                DrawAll();
            }
            else {
                var Proc = new TProc(_FLOW, null, _MOVETYPE);
                Proc.X = parseInt(obj.style.left);
                Proc.Y = parseInt(obj.style.top);
                Proc.Width = parseInt(obj.style.width);
                Proc.Height = parseInt(obj.style.height);
                if (Proc.Width < 20 || Proc.Height < 20) {
                    switch (_MOVETYPE) {
                        case'rect':
                            Proc.Width = '35px';
                            Proc.Height = '100px';
                            break;
                        case'diamond':
                            Proc.Width = '100px';
                            Proc.Height = '60px';
                            break;
                        default:
                            Proc.Width = '100px';
                            Proc.Height = '40px';
                            break;
                    }
                }
                _FLOW.addProc(Proc);
                pushLog("addproc", Proc);
                DrawAll();
            }
            break;
    }
    _MOVETYPE = "";
    _MOVELINEOBJ = null;
    _MOVEOBJ = null;
    return true;
}
function doDocSelectStart() {
    var oEvt = event.srcElement.tagName;
    if (oEvt != "INPUT") {
        return false;
    }
}
function doDocKeyDown() {
    switch (event.keyCode) {
        case 46:
            if (_FOCUSTEDOBJ != null && event.srcElement.tagName != "INPUT" && event.srcElement.tagName != "SELECT") {
                mnuDelObj();
            }
            break;
    }
}
function changeToSelectBtn() {
    var obj = document.getElementById('tbxToolbox_select');
    var objs = document.all("tbxToolbox_btn");
    for (var i = 0;
         i < objs.length;
         i++) {
        objs[i].className = "bon2";
    }
    obj.className = "bon1";
    _TOOLTYPE = obj.cType;
    document.all.Canvas.style.cursor = "default";
    return false;
}
document.onselectstart = doDocSelectStart;
document.onmousedown = doDocMouseDown;
document.onmousemove = doDocMouseMove;
document.onmouseup = doDocMouseUp;
document.onkeydown = doDocKeyDown;
document.onerror = new Function("return false;");
document.oncontextmenu = changeToSelectBtn;

var _PopTimer = 0;
function contextMenu() {
    this.items = new Array();
    this.addItem = function (item) {
        this.items[this.items.length] = item;
    }
    this.show = function (oDoc) {
        var S = "";
        var i;
        S = "<div id=\"rightmenu\" style=\"BACKGROUND-COLOR: #ffffff; BORDER: #000000 1px solid; LEFT: 0px; POSITION: absolute; TOP: 0px; VISIBILITY: hidden; Z-INDEX: 10\">";
        S += "<table class=\"menutable\" border=\"0\" height=\"";
        S += this.items.length * 20;
        S += "\" cellpadding=\"0\" cellspacing=\"0\">";
        S += "<tr height=\"3\"><td bgcolor=\"#d0d0ce\" width=\"2\"></td><td>";
        S += "<table class=\"menutable\" border=\"0\" width=\"100%\" height=\"100%\" cellpadding=0 cellspacing=0 bgcolor=\"#ffffff\">";
        S += "<tr><td bgcolor=\"#d0d0ce\" width=\"23\"></td><td><img src=\" \" height=\"1\" border=\"0\"></td></tr></table>";
        S += "</td><td width=\"2\"></td></tr>";
        S += "<tr><td bgcolor=\"#d0d0ce\"></td><td>";
        S += "<table class=\"menutable\" border=\"0\" width=\"100%\" height=\"100%\" cellpadding=3 cellspacing=0 bgcolor=\"#ffffff\">";
        oDoc.write(S);
        for (i = 0;
             i < this.items.length;
             i++) {
            this.items[i].show(oDoc);
        }
        S = "</table></td><td></td></tr>";
        S += "<tr height=\"3\"><td bgcolor=\"#d0d0ce\"></td><td>";
        S += "<table class=\"menutable\" border=\"0\" width=\"100%\" height=\"100%\" cellpadding=0 cellspacing=0 bgcolor=\"#ffffff\">";
        S += "<tr><td bgcolor=\"#d0d0ce\" width=\"23\"></td><td><img src=\" \" height=\"1\" border=\"0\"></td></tr></table>";
        S += "</td><td></td></tr>";
        S += "</table></div>\n";
        oDoc.write(S);
    }
}
function contextItem(text, icon, cmd, type) {
    this.text = text ? text : "";
    this.icon = icon ? icon : "";
    this.cmd = cmd ? cmd : "";
    this.type = type ? type : "menu";
    this.show = function (oDoc) {
        var S = "";
        if (this.type == "menu") {
            S += "<tr ";
            S += "onmouseover=\"changeStyle(this, 'on');\" ";
            S += "onmouseout=\"changeStyle(this, 'out');\" ";
            S += "onclick=\"hideMenu();";
            S += this.cmd;
            S += "\">";
            S += "<td class=\"ltdexit\" width=\"16\">";
            if (this.icon == "") {
                S += "&nbsp;";
            }
            else {
                S += "<img border=\"0\" src=\"";
                S += this.icon;
                S += "\" style=\"POSITION: relative\"></img>";
            }
            S += "</td><td class=\"mtdexit\">";
            S += this.text;
            S += "</td><td class=\"rtdexit\" width=\"5\">&nbsp;</td></tr>";
        }
        else {
            if (this.type == "separator") {
                S += "<tr><td class=\"ltdexit\">&nbsp;</td>";
                S += "<td class=\"mtdexit\" colspan=\"2\"><hr color=\"#000000\" size=\"1\"></td></tr>";
            }
        }
        oDoc.write(S);
    }
}
function changeStyle(obj, cmd) {
    if (obj) {
        try {
            var imgObj = obj.children(0).children(0);
            if (cmd == 'on') {
                obj.children(0).className = "ltdfocus";
                obj.children(1).className = "mtdfocus";
                obj.children(2).className = "rtdfocus";
                if (imgObj) {
                    if (imgObj.tagName.toUpperCase() == "IMG") {
                        imgObj.style.left = "-1px";
                        imgObj.style.top = "-1px";
                    }
                }
            }
            else {
                if (cmd == 'out') {
                    obj.children(0).className = "ltdexit";
                    obj.children(1).className = "mtdexit";
                    obj.children(2).className = "rtdexit";
                    if (imgObj) {
                        if (imgObj.tagName.toUpperCase() == "IMG") {
                            imgObj.style.left = "0px";
                            imgObj.style.top = "0px";
                        }
                    }
                }
            }
        }
        catch (e) {
        }
    }
}
function StopTimeout() {
    clearTimeout(_PopTimer);
}
function StartTimeout() {
    if (_PopTimer == 0) {
        _PopTimer = setTimeout('hideMenu()', 500);
    }
}
function showMenu() {
    var x, y, w, h, ox, oy;
    x = event.clientX;
    y = event.clientY;
    var obj = document.getElementById("rightmenu");
    if (obj == null) {
        return true;
    }
    ox = document.body.clientWidth;
    oy = document.body.clientHeight;
    if (x > ox || y > oy) {
        return false;
    }
    w = obj.offsetWidth;
    h = obj.offsetHeight;
    if ((x + w) > ox) {
        x = x - w;
    }
    if ((y + h) > oy) {
        y = y - h;
    }
    obj.style.posLeft = x + document.body.scrollLeft;
    obj.style.posTop = y + document.body.scrollTop;
    obj.style.visibility = "visible";
    return false;
}
function hideMenu() {
    if (event.button == 0) {
        var obj = document.getElementById("rightmenu");
        if (obj == null) {
            return true;
        }
        obj.style.visibility = "hidden";
        obj.style.posLeft = 0;
        obj.style.posTop = 0;
    }
}
function writeContextStyle() {
    var S = "";
    S += "<STYLE type=text/css>";
    S += ".menutable {Font-FAMILY: \"Tahoma\",\"Verdana\",\"宋体\"; FONT-SIZE: 9pt}";
    S += ".mtdfocus {BACKGROUND-COLOR: #ccccff; BORDER-BOTTOM: #000000 1px solid; BORDER-TOP: #000000 1px solid; CURSOR: hand}";
    S += ".mtdexit {BACKGROUND-COLOR: #ffffff; BORDER-BOTTOM: #ffffff 1px solid; BORDER-TOP: #ffffff 1px solid}";
    S += ".ltdfocus {BACKGROUND-COLOR: #ccccff; BORDER-BOTTOM: #000000 1px solid; BORDER-TOP: #000000 1px solid; BORDER-LEFT: #000000 1px solid; CURSOR: hand}";
    S += ".ltdexit {BACKGROUND-COLOR: #d0d0ce; BORDER-BOTTOM: #d0d0ce 1px solid; BORDER-TOP: #d0d0ce 1px solid; BORDER-LEFT: #d0d0ce 1px solid}";
    S += ".rtdfocus {BACKGROUND-COLOR: #ccccff; BORDER-BOTTOM: #000000 1px solid; BORDER-TOP: #000000 1px solid; BORDER-RIGHT: #000000 1px solid; CURSOR: hand}";
    S += ".rtdexit {BACKGROUND-COLOR: #ffffff; BORDER-BOTTOM: #ffffff 1px solid; BORDER-TOP: #ffffff 1px solid; BORDER-RIGHT: #ffffff 1px solid}";
    S += "</STYLE>";
    document.write(S);
}
function toggleMenu(isEnable) {
    if (isEnable) {
        document.oncontextmenu = showMenu;
    }
    else {
        document.oncontextmenu = new function () {
            return true;
        };
    }
}
writeContextStyle();
function DrawContextMenu() {
    var popmnu, item;
    popmnu = new contextMenu();
    item = new contextItem("新建流程图", "image/new.gif", "mnuNewFlow();", "menu");
    popmnu.addItem(item);
    item = new contextItem("打开流程图", "image/open.gif", "mnuOpenFlow();", "menu");
    popmnu.addItem(item);
    item = new contextItem("保存流程图", "image/save.gif", "mnuSaveFlow();", "menu");
    popmnu.addItem(item);
    item = new contextItem("流程图属性", "image/edit.gif", "mnuEditFlow();", "menu");
    popmnu.addItem(item);
    item = new contextItem("重新载入流程图", "image/refresh.gif", "mnuReloadFlow();", "menu");
    popmnu.addItem(item);
    item = new contextItem("", "", "", "separator");
    popmnu.addItem(item);
    item = new contextItem("新建[任务]", "image/add_proc.gif", "mnuAddProc();", "menu");
    popmnu.addItem(item);
    item = new contextItem("新建[路径]", "image/add_step.gif", "mnuAddStep();", "menu");
    popmnu.addItem(item);
    item = new contextItem("复制选中任务", "image/copy.gif", "mnuCopyProc();", "menu");
    popmnu.addItem(item);
    item = new contextItem("编辑选中对象", "image/edit_obj.gif", "mnuEditObj();", "menu");
    popmnu.addItem(item);
    item = new contextItem("删除选中对象", "image/del_obj.gif", "mnuDelObj();", "menu");
    popmnu.addItem(item);
    item = new contextItem("移到最前面", "image/front.gif", "mnuSetZIndex('F');", "menu");
    popmnu.addItem(item);
    item = new contextItem("移到最后面", "image/back.gif", "mnuSetZIndex('B');", "menu");
    popmnu.addItem(item);
    popmnu.show(this.document);
    delete item;
    delete popmnu;
}

TTopFlow.prototype.formatXml = function (text) {
    text = '\n' + text.replace(/(<\w+)(\s.*?>)/g,
        function ($0, name, props) {
            return name + ' ' + props.replace(/\s+(\w+=)/g, " $1");
        }).replace(/>\s*?</g, ">\n<");
    text = text.replace(/\n/g, '\r').replace(/<!--(.+?)-->/g,
        function ($0, text) {
            var ret = '<!--' + escape(text) + '-->';
            return ret;
        }).replace(/\r/g, '\n');
    var rgx = /\n(<(([^\?]).+?)(?:\s|\s*?>|\s*?(\/)>)(?:.*?(?:(?:(\/)>)|(?:<(\/)\2>)))?)/mg;
    var nodeStack = [];
    var commitflow = this;
    var output = text.replace(rgx, function ($0, all, name, isBegin, isCloseFull1, isCloseFull2, isFull1, isFull2) {
        var isClosed = (isCloseFull1 == '/') || (isCloseFull2 == '/') || (isFull1 == '/') || (isFull2 == '/');
        var prefix = '';
        if (isBegin == '!') {
            prefix = commitflow.getPrefix(nodeStack.length);
        }
        else {
            if (isBegin != '/') {
                prefix = commitflow.getPrefix(nodeStack.length);
                if (!isClosed) {
                    nodeStack.push(name);
                }
            }
            else {
                nodeStack.pop();
                prefix = commitflow.getPrefix(nodeStack.length);
            }
        }
        var ret = '\n' + prefix + all;
        return ret;
    });
    var prefixSpace = -1;
    var outputText = output.substring(1);
    outputText = outputText.replace(/\n/g, '\r').replace(/(\s*)<!--(.+?)-->/g, function ($0, prefix, text) {
        if (prefix.charAt(0) == '\r') {
            prefix = prefix.substring(1);
        }
        text = unescape(text).replace(/\r/g, '\n');
        var ret = '\n' + prefix + '<!--' + text.replace(/^\s/mg, prefix) + '-->';
        return ret;
    });
    return outputText.replace(/\s+$/g, '').replace(/\r/g, '\r\n');
}
TTopFlow.prototype.getPrefix = function (prefixIndex) {
    var span = '    ';
    var output = [];
    for (var i = 0;
         i < prefixIndex;
         ++i) {
        output.push(span);
    }
    return output.join('');
}