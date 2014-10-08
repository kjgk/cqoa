<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/tld/taglibs.jsp" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<html xmlns:v="urn:schemas-microsoft-com:vml">
<head>
<title>工作流流程图设计</title>
<link href="${path}/css/workflow.css" type="text/css" rel="stylesheet">

<script type="text/javascript" src="${path}/scripts/withub/ext/util/string.js"></script>
<script language="JavaScript" src="${path}/scripts/control/workflow/TProc.js"></script>
<script language="JavaScript" src="${path}/scripts/control/workflow/TStep.js"></script>
<script language="JavaScript" src="${path}/scripts/control/workflow/TToolBox.js"></script>
<script language="JavaScript" src="${path}/scripts/control/workflow/TToolPanel.js"></script>
<script language="JavaScript" src="${path}/scripts/control/workflow/TTopFlow.js"></script>
<script language="JavaScript" src="${path}/scripts/control/workflow/flow.js"></script>

<script type="text/javascript" language="javascript" src="${path}/dwr/interface/ajaxService.js"></script>
<script type="text/javascript" language="javascript" src="${path}/dwr/engine.js"></script>
<script type="text/javascript" language="javascript" src="${path}/dwr/util.js"></script>
<script>
<!--
if ((navigator.userAgent.toLowerCase().indexOf("msie") < 0)) {
    alert('本流程图设计使用VML,只能在IE下使用！');
}
//startup
var _TREE;
//function
function DrawTree() {
    _TREE = new dTree('_TREE', '${path}/images/dtree/');
    var num = 3,obj,i;
    var FocusColor = _FLOW.Config.ProcFocusedStrokeColor;
    _TREE.add(0, -1, ' ' + _FLOW.Text + '', 'javascript:', '', _FLOW.ID);
    _TREE.add(1, 0, '任务', 'javascript:', '', 'Procs', '', '${path}/images/workflow/task.gif', '${path}/images/workflow/task.gif');
    _TREE.add(2, 0, '路径', 'javascript:', '', 'Steps', '', '${path}/images/workflow/step.gif', '${path}/images/workflow/step.gif');
    for (i = 0;
         i < _FLOW.Procs.length;
         i++) {
        obj = _FLOW.Procs[i];
        _TREE.add(num++, 1, obj.Text, 'javascript:objFocusedOn("' + obj.ID + '");', '', obj.ID, '', '${path}/images/workflow/obj4.gif', '${path}/images/workflow/obj4.gif');
    }
    for (i = 0;
         i < _FLOW.Steps.length;
         i++) {
        obj = _FLOW.Steps[i];
        _TREE.add(num++, 2, obj.Text, 'javascript:objFocusedOn("' + obj.ID + '");', '', obj.ID, '', '${path}/images/workflow/obj4.gif', '${path}/images/workflow/obj4.gif');
    }
    treeview.innerHTML = _TREE;
}
function DrawDataView() {
    var idTR,idTD,arr = _FLOW.DataView();
    for (var i = dataview.rows.length - 1;
         i > 0;
         i--)
        dataview.deleteRow(i);
    for (var i = 0,u = 1;
         i < arr.length;
         i++) {
        idTR = dataview.insertRow();
        idTR.onmouseover = new Function("this.className=\"focusLine\";");
        idTR.onmouseout = new Function("this.className=\"normalLine\";");
        idTR.height = "22";
        idTD = idTR.insertCell();
        idTD.innerHTML = arr[i].ProcID + "(" + arr[i].ProcText + ")";
        idTD = idTR.insertCell();
        idTD.innerHTML = arr[i].Idx;
        idTD = idTR.insertCell();
        idTD.innerHTML = arr[i].PreProcID + "(" + arr[i].PreProcText + ")";
        idTD = idTR.insertCell();
        idTD.innerHTML = arr[i].Cond + "&nbsp;";
    }
    mergecell(dataview);
}
function DrawVML() {
    Canvas.innerHTML = _FLOW.ProcString();
    Canvas.innerHTML += _FLOW.StepString();
    _FLOW.getInnerObject();
    _FOCUSTEDOBJ = null;
    stuffProp();
}
function DrawAll() {
    //DrawTree();
    DrawVML();
    //DrawDataView();
}
function LoadFlow(AUrl) {

    if (AUrl == "")
        _FLOW.createNew("");
    else
        _FLOW.loadFromXML(AUrl);
    DrawAll();
    emptyLog();
}
function ObjSelected() {
    if (_FOCUSTEDOBJ == null) {
        alert("当前没有选中对象！--用鼠标单击流程图上的对象可以选中它");
        return false;
    }
    return true;
}
//[流程图]菜单处理事件
function mnuNewFlow() {
    if (_FLOW.Modified)
        if (!confirm("当前流程图尚未保存，新建新文件将会放弃所有修改，继续新建吗？"))return;
    var flow = new TTopFlow("");
    flow.FileName = "untitled.xml";
    if (openWin("flow.htm", flow, 350, 200)) {
        LoadFlow("");
        _FLOW.FileName = flow.FileName;
        _FLOW.Text = flow.Text;
        _FLOW.Password = flow.Password;
        DrawTree();
        delete flow;
    }
}
function mnuEditFlow() {
    if (openWin("flow.htm", _FLOW, 350, 200)) {
        DrawTree();
    }
}
function mnuValidateFlow() {
    var s = _FLOW.validate();
    if (s == "")
        alert("校验完成，这是一个合法的流程图！");
    else
        alert(s);
}
function mnuOpenFlow() {
    if (_FLOW.Modified)
        if (!confirm("当前流程图尚未保存，打开新文件将会放弃所有修改，继续打开吗？"))return;
    if (openWin("filelist.jsp", _FLOW, 450, 400))
        LoadFlow(_FLOW.FileName);
}
function mnuSaveFlow() {
    if (!confirm("确定要保存当前流程图[" + _FLOW.FileName + "]吗？"))return;
    var s = _FLOW.validate();
    if (s != "")
        if (!confirm("当前有效性检查有误\n\n" + s + "\n\n是否要继续保存？"))return;
    /*try{
     var s=_FLOW.SaveToXML("savexml.jsp");
     if(s=="")
     alert("保存成功！")
     else
     alert("[保存失败]");
     }
     catch(e) {
     alert(e);
     }*/
    var xml = _FLOW.getConfigXml();
    ajaxService.saveWorkflowConfig("${flowType.objectId}", xml, saveHanlderCallback);
}

function saveHanlderCallback(data) {
    if (data == 1) {
        alert("保存成功.");
    } else {
        alert("保存失败.");
    }
}

function mnuReloadFlow() {
    if (_FLOW.Modified)
        if (!confirm("当前流程图尚未保存，重新载入将会放弃所有修改，继续重载吗？"))return;
    LoadFlow(_FLOW.FileName);
}
//[流程图对象]菜单处理事件
function mnuAddProc() {
    var Proc = new TProc(_FLOW);
    if (openWin("proc.htm", Proc, 450, 350)) {
        _FLOW.addProc(Proc);
        pushLog("addproc", Proc);
        DrawAll();
    }
}
function mnuAddStep() {
    var Step = new TStep(_FLOW);
    if (openWin("step.htm", Step, 500, 350)) {
        _FLOW.addStep(Step);
        pushLog("addstep", Step);
        DrawAll();
    }
}
function mnuCopyProc() {
    if (!ObjSelected())return;
    if (_FOCUSTEDOBJ.typ != "Proc") {
        alert("只有任务可以复制！");
        return;
    }
    var curProc = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
    if (!confirm("确定要复制任务[" + curProc.Text + "]吗？"))return;
    var Proc = new TProc(_FLOW);
    var iID = Proc.ID;
    Proc.clone(curProc);
    Proc.ID = iID;
    Proc.X = parseInt(curProc.X) + 10;
    Proc.Y = parseInt(curProc.Y) + 10;
    _FLOW.addProc(Proc);
    pushLog("addproc", Proc);
    DrawAll();
    objFocusedOn(iID);
    mnuSetZIndex("F");
}
function mnuEditObj() {
    if (!ObjSelected())return;
    if (_FOCUSTEDOBJ.typ != "Proc" && _FOCUSTEDOBJ.typ != "Step")return;
    if (_FOCUSTEDOBJ.typ == "Proc")
        editProc(_FOCUSTEDOBJ.id);
    else
        editStep(_FOCUSTEDOBJ.id);
}
function mnuDelObj() {
    if (ObjSelected())deleteObj(_FOCUSTEDOBJ.id);
}
function mnuSetZIndex(Act) {
    if (!ObjSelected())return;
    if (_FOCUSTEDOBJ.typ != "Proc" && _FOCUSTEDOBJ.typ != "Step")return;
    if (_FOCUSTEDOBJ.typ == "Proc")
        var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
    else
        var obj = _FLOW.getStepByID(_FOCUSTEDOBJ.id);
    var oldValue = obj.zIndex;
    if (Act == "F")
        _FLOW.brintToFront(obj);
    else
        _FLOW.sendToBack(obj);
    _FOCUSTEDOBJ.style.zIndex = obj.zIndex;
    pushLog("editprop", {
        "obj":obj,"prop":"zIndex","_old":oldValue,"_new":obj.zIndex
    });
}
//[系统菜单]处理事件
function mnuOption() {
    if (openWin("option.htm", _FLOW.Config, 510, 510)) {
        DrawAll();
    }
}
function mnuDemo() {
    var tmpwin = window.open("demo.htm");
    tmpwin.focus();
}
function mnuFileMgr() {
    openWin("filemgr.jsp", "", 450, 400);
}
function mnuAbout() {
    openWin("about.htm", "", 480, 460);
}
function mnuExit() {
    if (confirm("确定要退出本系统吗？")) {
        window.opener = null;
        window.close();
    }
}
function mnuSetZoom(Act) {
    var rate = Act == "in" ? 0.2 : -0.2;
    var newzoom = _ZOOM + rate;
    if (newzoom > 2)return;
    if (newzoom < 0.2)return;
    changeZoom(newzoom);
    document.all("zoomshow").value = _ZOOM;
}
function changeZoom(v) {
    _ZOOM = parseFloat(parseFloat(v).toFixed(2));
    Canvas.style.zoom = _ZOOM;
}
function mnuTurnToolbox() {
    tbxToolbox.VisibleEx = !tbxToolbox.VisibleEx;
    tbxToolbox.InnerObject.style.display = tbxToolbox.VisibleEx ? "block" : "none";
    document.all.mnu_win_toolbox.innerHTML = (tbxToolbox.VisibleEx ? "隐藏" : "显示") + "工具箱";
}
function mnuTurnPropbox() {
    tbxPropbox.VisibleEx = !tbxPropbox.VisibleEx;
    tbxPropbox.InnerObject.style.display = tbxPropbox.VisibleEx ? "block" : "none";
    document.all.mnu_win_propbox.innerHTML = (tbxPropbox.VisibleEx ? "隐藏" : "显示") + "属性框";
}
function mnuTurnObjView() {
    tbxObjView.VisibleEx = !tbxObjView.VisibleEx;
    tbxObjView.InnerObject.style.display = tbxObjView.VisibleEx ? "block" : "none";
    document.all.mnu_win_objview.innerHTML = (tbxObjView.VisibleEx ? "隐藏" : "显示") + "对象视图";
}
function mnuTurnDataView() {
    tbxDataView.VisibleEx = !tbxDataView.VisibleEx;
    tbxDataView.InnerObject.style.display = tbxDataView.VisibleEx ? "block" : "none";
    document.all.mnu_win_dataview.innerHTML = (tbxDataView.VisibleEx ? "隐藏" : "显示") + "数据视图";
}
function mnuViewXmlObj() {
    document.getElementById('jbpmxml').value = _FLOW.getJbpmXml();
    document.getElementById('xml').style.display = '';
    document.onselectstart = cancelPanreEvent;
    document.onmousedown = cancelPanreEvent;
    document.onmousemove = cancelPanreEvent;
    document.onmouseup = cancelPanreEvent;
    document.onkeydown = cancelPanreEvent;
    //Ext.Cmt.alert(_FLOW.getJbpmXml());
}

function cancelPanreEvent() {
    window.event.cancelBubble = true;
}

//-->
</SCRIPT>
</HEAD>
<BODY leftmargin="0" topmargin="0" marginwidth="0" marginheight="0"
      style='background-image: url(${path}/images/workflow/canvasbg.gif);'>

<div style='left:0px;top:0px;width:100%;height:100%;position:absolute;z-index:-1000' id="Canvas"></div>
<v:rect class="toolui" style="display:none" id="_rectui">
    <v:Stroke dashstyle="dashdot"/>
</v:rect>
<v:roundrect class="toolui" style="display:none;left:0px;top:0px;width:60px;height:50px;" id="_roundrectui">
    <v:Stroke dashstyle="dashdot"/>
</v:roundrect>
<v:shape type="#diamond" class="toolui" style="display:none;left:0px;top:0px;width:60px;height:50px;" strokeweight="1px"
         id="_diamondui">
    <v:Stroke dashstyle="dashdot"/>
</v:shape>
<v:oval class="toolui" style="display:none;left:0px;top:0px;width:60px;height:50px;" id="_ovalui">
    <v:Stroke dashstyle="dashdot"/>
</v:oval>
<v:line class="toolui" style="display:none" from="0,0" to="100,0" id="_lineui">
    <v:Stroke dashstyle="dashdot" StartArrow="" EndArrow="Classic"/>
</v:line>
<script language="JavaScript">
    <!--
    function NavBrush_onClick(obj) {
        //清除所有的ButtonClass
        var objs = document.all("tbxToolbox_btn");
        for (var i = 0;
             i < objs.length;
             i++)
            objs[i].className = "bon2";
        obj.className = "bon1";
        _TOOLTYPE = obj.cType;
    }

    S = '<table width="100%" height="100%" cellspacing="0" cellpadding="0">' +
            '<tr>' +
            '<td bgcolor="buttonface" valign="top" align="center">' +
            '<div id="tableContainer2" class="tableContainer">' +
            '<table id="propview" border="0" cellpadding="0" cellspacing="0" width="100%" class="scrollTable"> ' +
            '<thead class="fixedHeader2"> ' +
            '<tr height="22" bgcolor="#CCCCFF">' +
            '<td width="90">属性</td><td>值</td>' +
            '</tr>' +
            '</thead> ' +
            '<tbody class="scrollContent"> ' +
            '</tbody> ' +
            '</table> ' +
            '</div> ' +
            '</td>' +
            '</tr>' +
            '</table>';
    tbxPropbox = new TToolBox("tbxPropbox", "属性", document.body.offsetWidth - 270, 25, 250, 355, S, "down", true);
    var S = '<table cellspacing="0" cellpadding="0" border="0">' +
            '<tr height="25"><td width="2"></td>' +
        //'<td width="20" align="center" title="新建流程图 Ctrl+N"><BUTTON class="normalBtn" onmouseover="this.className=\'focusBtn\'" onmouseout=\"this.className=\'normalBtn\'" HIDEFOCUS="true" onclick="mnuNewFlow();"><img src="image/new.gif" border="0"></BUTTON></td>' +
        //'<td width="20" align="center" title="打开流程图 Ctrl+O"><BUTTON class="normalBtn" onmouseover="this.className=\'focusBtn\'" onmouseout=\"this.className=\'normalBtn\'" HIDEFOCUS="true" onclick="mnuOpenFlow();"><img src="image/open.gif" border="0"></BUTTON></td>' +
            '<td width="20" align="center" title="保存流程图 Ctrl+S"><BUTTON class="normalBtn" onmouseover="this.className=\'focusBtn\'" onmouseout=\"this.className=\'normalBtn\'" HIDEFOCUS="true" onclick="mnuSaveFlow();"><img src="${path}/images/workflow/save.gif" border="0"></BUTTON></td>' +
            '<td width="20" align="center" title="校验流程图"><BUTTON class="normalBtn" onmouseover="this.className=\'focusBtn\'" onmouseout=\"this.className=\'normalBtn\'" HIDEFOCUS="true" onclick="mnuValidateFlow();"><img src="${path}/images/workflow/validate.gif" border="0"></BUTTON></td>' +
        //'<td width="20" align="center" title="流程图属性"><BUTTON class="normalBtn" onmouseover="this.className=\'focusBtn\'" onmouseout=\"this.className=\'normalBtn\'" HIDEFOCUS="true" onclick="mnuOption();"><img src="image/edit.gif" border="0"></BUTTON></td>'+
        //'<td width="20" align="center" title="重新载入流程图"><BUTTON class="normalBtn" onmouseover="this.className=\'focusBtn\'" onmouseout=\"this.className=\'normalBtn\'" HIDEFOCUS="true" onclick="mnuReloadFlow();"><img src="${path}/images/workflow/refresh.gif" border="0"></BUTTON></td>'+
            '<td width="4" background="image/split.gif"></td>' +
        //'<td width="23" align="center" title="新建[任务]"><BUTTON class="normalBtn" onmouseover="this.className=\'focusBtn\'" onmouseout=\"this.className=\'normalBtn\'" HIDEFOCUS="true" onclick="mnuAddProc();"><img src="${path}/images/workflow/add_proc.gif" border="0"></BUTTON></td>' +
        //'<td width="23" align="center" title="新建[路径]"><BUTTON class="normalBtn" onmouseover="this.className=\'focusBtn\'" onmouseout=\"this.className=\'normalBtn\'" HIDEFOCUS="true" onclick="mnuAddStep();"><img src="${path}/images/workflow/add_step.gif" border="0"></BUTTON></td>' +
        //'<td width="23" align="center" title="复制选中任务 Ctrl+C"><BUTTON class="normalBtn" onmouseover="this.className=\'focusBtn\'" onmouseout=\"this.className=\'normalBtn\'" HIDEFOCUS="true" onclick="mnuCopyProc();"><img src="${path}/images/workflow/copy.gif" border="0"></BUTTON></td>' +

        //'<td width="23" align="center" title="修改选中对象"><BUTTON class="normalBtn" onmouseover="this.className=\'focusBtn\'" onmouseout=\"this.className=\'normalBtn\'" HIDEFOCUS="true" onclick="mnuEditObj();"><img src="${path}/images/workflow/edit_obj.gif" border="0"></BUTTON></td>' +
            '<td width="2" align="center" title="选择对象\n\n1.单击本按钮\n2.在工作区(画布)上单击[任务]或[路径]的图形"><BUTTON cType="point" class="bon1" id="tbxToolbox_select" name="tbxToolbox_btn" HIDEFOCUS="true" onclick="NavBrush_onClick(this);"><v:PolyLine filled="false" Points="0,0 0,16 5,12 9,20 12,18 8,10 12,8 0,0" style="position:relative;" strokeweight="1"/></BUTTON></td>' +
            '<td align="center"></td>' +
            '' +
            '<td align="center" title="新增[任务](圆角形)\n\n1.单击本按钮\n2.在工作区(画布)上空白位置按住左键\n3.拉动鼠标\n4.松开鼠标左键"><BUTTON cType="roundrect" class="bon2" name="tbxToolbox_btn"  HIDEFOCUS="true" onclick="NavBrush_onClick(this);"><v:RoundRect style="position:relative;left:1px;top:1px;width:20px;height:20px;" strokeweight="1" filled="F"></v:RoundRect></BUTTON></td>' +
            '' +
        //'<td width="20" align="center" title="新增[分支节点](菱形)\n\n1.单击本按钮\n2.在工作区(画布)上空白位置按住左键\n3.拉动鼠标\n4.松开鼠标左键"><BUTTON cType="diamond" class="bon2" name="tbxToolbox_btn"  HIDEFOCUS="true" onclick="NavBrush_onClick(this);"><v:shape type="#diamond" style="position:relative;width:20;height:20;left:0;top:0;" strokeweight="1px" filled="F"></v:shape></BUTTON></td>'+
        //'<td width="20" align="center" title="新增[汇聚节点](方形)\n\n1.单击本按钮\n2.在工作区(画布)上空白位置按住左键\n3.拉动鼠标\n4.松开鼠标左键"><BUTTON cType="rect" class="bon2" name="tbxToolbox_btn"  HIDEFOCUS="true" onclick="NavBrush_onClick(this);"><v:rect filled="F" style="position:relative;left:1px;top:1px;width:10px;height:20px;background-color:#cccccc;" strokeweight="1px"/></BUTTON></td>'+
            '<td width="20" align="center" title="新增[路径](折角线)\n\n1.单击本按钮\n2.在[任务]图形(起点)上按住鼠标左键\n3.拉动鼠标至某[任务]图形(终点)中间位置\n4.松开鼠标左键"><BUTTON cType="polyline" class="bon2" name="tbxToolbox_btn"  HIDEFOCUS="true" onclick="NavBrush_onClick(this);"><v:PolyLine filled="false" Points="0,20 10,5 20,20" style="position:relative;" strokeweight="1"><v:stroke EndArrow="Classic"/></v:PolyLine></BUTTON></td>' +
            '<td width="4" background="image/split.gif"></td>' +
            '<td width="23" align="center" title="删除选中对象 Del"><BUTTON class="normalBtn" onmouseover="this.className=\'focusBtn\'" onmouseout=\"this.className=\'normalBtn\'" HIDEFOCUS="true" onclick="mnuDelObj();"><img src="${path}/images/workflow/del_obj.gif" border="0"></BUTTON></td>' +
            '<td width="4" background="image/split.gif"></td>' +
            '<td width="23" align="center" title="放大显示比例 Ctrl++"><BUTTON class="normalBtn" onmouseover="this.className=\'focusBtn\'" onmouseout=\"this.className=\'normalBtn\'" HIDEFOCUS="true" onclick="mnuSetZoom(\'in\');"><img src="${path}/images/workflow/zoomin.gif" border="0"></BUTTON></td>' +
            '<td width="35" align="center" title="显示比例"><select id="zoomshow" style="width:50px;" onchange="changeZoom(this.value);"><option value="0.2">20%</option><option value="0.4">40%</option><option value="0.6">60%</option><option value="0.8">80%</option><option value="1" selected>100%</option><option value="1.2">120%</option><option value="1.4">140%</option><option value="1.6">160%</option><option value="1.8">180%</option><option value="2">200%</option></select></td>' +
            '<td width="23" align="center" title="缩小显示比例 Ctrl+-"><BUTTON class="normalBtn" onmouseover="this.className=\'focusBtn\'" onmouseout=\"this.className=\'normalBtn\'" HIDEFOCUS="true" onclick="mnuSetZoom(\'out\');"><img src="${path}/images/workflow/zoomout.gif" border="0"></BUTTON></td>' +
            '<td width="23" align="center" title="将选中对象置于最顶层"><BUTTON class="normalBtn" onmouseover="this.className=\'focusBtn\'" onmouseout=\"this.className=\'normalBtn\'" HIDEFOCUS="true" onclick="mnuSetZIndex(\'F\');"><img src="${path}/images/workflow/front.gif" border="0"></BUTTON></td>' +
            '<td width="23" align="center" title="将选中对象置于最底层"><BUTTON class="normalBtn" onmouseover="this.className=\'focusBtn\'" onmouseout=\"this.className=\'normalBtn\'" HIDEFOCUS="true" onclick="mnuSetZIndex(\'B\');"><img src="${path}/images/workflow/back.gif" border="0"></BUTTON></td>' +
            '<td width="23" align="center" title="撤消最后一次操作 Ctrl+Z"><BUTTON class="normalBtn" onmouseover="this.className=\'focusBtn\'" onmouseout=\"this.className=\'normalBtn\'" HIDEFOCUS="true" onclick="undoLog();"><img src="${path}/images/workflow/undo.gif" border="0"></BUTTON></td>' +
            '<td width="23" align="center" title="恢复最后一次取消的操作 Ctrl+Y/Ctrl+Shift+Z"><BUTTON class="normalBtn" onmouseover="this.className=\'focusBtn\'" onmouseout=\"this.className=\'normalBtn\'" HIDEFOCUS="true" onclick="redoLog();"><img src="${path}/images/workflow/redo.gif" border="0"></BUTTON></td>' +
            '<td width="4" background="image/split.gif"></td>' +
            '<td width="23" align="center" title="关于"><BUTTON class="normalBtn" onmouseover="this.className=\'focusBtn\'" onmouseout=\"this.className=\'normalBtn\'" HIDEFOCUS="true" onclick="mnuAbout();"><img src="${path}/images/workflow/qa.gif" border="0"></BUTTON></td>' +
            '<td width="2"></td></tr>' +
            '</table>';

    tbxToolbar = new TToolPanel("tbxToolbar", 1, 1, 500, 25, S);

    //tbxObjView.showBox();
    document.all("zoomshow").value = _ZOOM;
    //-->

</script>


<script type="text/javascript" language="JavaScript">

    var configInfo = '${configInfo}';

    if (configInfo == "") {
        LoadFlow("");
        _FLOW.FileName = "${flowType.name}";
    } else {
        _FLOW.loadXmlConfig("${flowType.name}", configInfo);
        DrawAll();
        emptyLog();
    }

</script>
</body>
</html>
