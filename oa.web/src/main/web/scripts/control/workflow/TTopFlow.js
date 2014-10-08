function TTopFlow(AName) {
    this.name = AName;
    this.ID = "";
    this.Text = "";
    this.FileName = "";
    this.FormID = "";
    this.Modified = false;
    this.Steps = [];
    this.Procs = [];
    this.Labels = [];
    this.SelectedObject = null;
    this.Password = "";
    this.Config = {_ProcColor: "#FF0000", _ProcTextColor: "#FF0000", ProcColor: "#0000FF", ProcTextColor: "#0000FF", ProcFocusedStrokeColor: "#00AA00", IsProcShadow: "T", ProcShadowColor: "#B3B3B3", ProcColor1: "#FFFFFF", ProcColor2: "#FFFFFF", JoinColor: "#f0f0f0", IsProc3D: "F", Proc3DDepth: "20", StepFocusedStrokeColor: "#00AA00", StepColor: "#0000FF"}
}
TTopFlow.prototype.getInnerObject = function () {
    for (var i = 0;
         i < this.Procs.length;
         i++) {
        this.Procs[i].getInnerObject();
    }
    for (i = 0;
         i < this.Steps.length;
         i++) {
        this.Steps[i].getInnerObject();
    }
}
TTopFlow.prototype.selectObject = function (aID, aType) {
    this.unSelectObject();
    this.SelectedObject = (aType == "Proc") ? this.getProcByID(aID) : this.getStepByID(aID);
    this.SelectedObject.setFocus();
}
TTopFlow.prototype.unSelectObject = function () {
    if (this.SelectedObject != null) {
        this.SelectedObject.lostFocus();
    }
    this.SelectedObject = null;
}
TTopFlow.prototype.clear = function () {
    this.FileName = '';
    this.Steps.length = 0;
    this.Procs.length = 0;
}
TTopFlow.prototype.addProc = function (AProc) {
    if (this.Procs.length > 100) {
        alert("最多不允许超过100个任务!");
        return false;
    }
    this.Modified = true;
    this.Procs[this.Procs.length] = AProc;
}
TTopFlow.prototype.addStep = function (AStep) {
    this.Steps[this.Steps.length] = AStep;
    this.Modified = true;
}
TTopFlow.prototype.changeProcID = function (OldID, NewID) {
    var Step;
    for (var i = 0;
         i < this.Steps.length;
         i++) {
        Step = this.Steps[i];
        if (Step.FromProc == OldID) {
            Step.FromProc = NewID;
        }
        if (Step.ToProc == OldID) {
            Step.ToProc = NewID;
        }
    }
}
TTopFlow.prototype.getProcDataView = function (AProcID) {
    var arr = [], Step;
    for (var i = 0;
         i < this.Steps.length;
         i++) {
        Step = this.Steps[i];
        if (Step.ToProc == AProcID) {
            S = this.getProcByID(Step.FromProc).Text;
            arr[arr.length] = new Array(Step.ID, S, Step.Cond);
        }
    }
    return arr;
}
TTopFlow.prototype.DataView = function () {
    var Proc;
    arrDataView = [], arr = [];
    var i, j, u, k = 0;
    for (i = 0;
         i < this.Procs.length;
         i++) {
        Proc = this.Procs[i];
        arr.length = 0;
        arr = this.getProcDataView(Proc.ID);
        u = arr.length;
        if (u != undefined && u != null && u > 0) {
            for (j = 0;
                 j < arr.length;
                 j++) {
                arrDataView[k++] = {"ProcID": Proc.ID, "ProcText": Proc.Text, "Idx": j + 1, "PreProcID": arr[j][0], "PreProcText": arr[j][1], "Cond": arr[j][2]}
            }
        }
    }
    return arrDataView;
}
TTopFlow.prototype.hasPriorProc = function (AProcID) {
    for (var i = 0;
         i < this.Steps.length;
         i++) {
        if (this.Steps[i].ToProc == AProcID) {
            return true;
        }
    }
    return false;
}
TTopFlow.prototype.hasNextProc = function (AProcID) {
    for (var i = 0;
         i < this.Steps.length;
         i++) {
        if (this.Steps[i].FromProc == AProcID) {
            return true;
        }
    }
    return false;
}
TTopFlow.prototype.validate = function () {
    var ErrMsg = [];
    WarnMsg = [];
    var Proc, PType;
    var iFork = 0, iJoin = 0;
    for (var i = 0;
         i < this.Procs.length;
         i++) {
        Proc = this.Procs[i];
        PType = (Proc.ProcType == "begin" ? "开始节点" : (Proc.ProcType == "end" ? "结束节点" : (Proc.ProcType == "fork" ? "分支节点" : ((Proc.ProcType == "andjoin" || Proc.ProcType == "unandjoin") ? "汇聚节点" : "任务节点"))));
        if (Proc.ProcType != "begin") {
            if (!this.hasPriorProc(Proc.ID)) {
                ErrMsg.push("[" + Proc.Text + "] - " + PType + "必须有输入路径");
            }
        }
        if (Proc.ProcType != "end") {
            if (!this.hasNextProc(Proc.ID)) {
                ErrMsg.push("[" + Proc.Text + "] - " + PType + "必须有输出路径");
            }
        }
        if (Proc.ProcType == 'andjoin' || Proc.ProcType == 'unandjoin') {
            iJoin++;
        }
        if (Proc.ProcType == 'fork') {
            iFork++;
        }
    }
    if (iJoin != iFork) {
        ErrMsg.push("[分支节点]和[汇聚节点]需要一一对应");
    }
    return ErrMsg.join("\n") + WarnMsg.join("\n");
}

TTopFlow.prototype.getProcByID = function (id) {
    for (var i = 0;
         i < this.Procs.length;
         i++) {
        if (this.Procs[i].ID == id) {
            return this.Procs[i];
        }
    }
    return null;
}
TTopFlow.prototype.getStepByID = function (id) {
    for (var i = 0;
         i < this.Steps.length;
         i++) {
        if (this.Steps[i].ID == id) {
            return this.Steps[i];
        }
    }
    return null;
}
TTopFlow.prototype.getProcAtXY = function (x, y) {
    var Proc;
    for (var i = 0;
         i < this.Procs.length;
         i++) {
        Proc = this.Procs[i];
        if (x >= parseInt(Proc.X) && x <= parseInt(Proc.X) + parseInt(Proc.Width) && y >= parseInt(Proc.Y) && y <= parseInt(Proc.Y) + parseInt(Proc.Height)) {
            return Proc;
        }
    }
    return null;
}
TTopFlow.prototype.IDExists = function (id) {
    var obj = _FLOW.getProcByID(id);
    if (obj != null) {
        return true;
    }
    var obj = _FLOW.getStepByID(id);
    return(obj != null);
}
TTopFlow.prototype.StepPathExists = function (FromProc, ToProc) {
    var Step;
    for (var i = 0;
         i < this.Steps.length;
         i++) {
        Step = this.Steps[i];
        if (Step.FromProc == FromProc && Step.ToProc == ToProc) {
            return Step;
        }
    }
    return null;
}
TTopFlow.prototype.deleteProcByID = function (id) {
    this.Modified = true;
    for (var i = 0;
         i < this.Procs.length;
         i++) {
        if (this.Procs[i].ID == id) {
            this.Procs.splice(i, 1);
        }
    }
    for (i = this.Steps.length - 1;
         i >= 0;
         i--) {
        if (this.Steps[i].FromProc == id || this.Steps[i].ToProc == id) {
            this.Steps.splice(i, 1);
        }
    }
}
TTopFlow.prototype.deleteStepByID = function (id) {
    this.Modified = true;
    for (var i = 0;
         i < this.Steps.length;
         i++) {
        if (this.Steps[i].ID == id) {
            this.Steps.splice(i, 1);
        }
    }
}
TTopFlow.prototype.getMaxZIndex = function () {
    var m = 0;
    for (var i = 0;
         i < this.Procs.length;
         i++) {
        m = Math.max(m, this.Procs[i].zIndex);
    }
    for (i = 0;
         i < this.Steps.length;
         i++) {
        m = Math.max(m, this.Steps[i].zIndex);
    }
    return m;
}
TTopFlow.prototype.getMinZIndex = function () {
    var m = 0;
    for (var i = 0;
         i < this.Procs.length;
         i++) {
        m = Math.min(m, this.Procs[i].zIndex);
    }
    for (i = 0;
         i < this.Steps.length;
         i++) {
        m = Math.min(m, this.Steps[i].zIndex);
    }
    return m;
}
TTopFlow.prototype.brintToFront = function (obj) {
    this.Modified = true;
    obj.zIndex = this.getMaxZIndex() + 1;
}
TTopFlow.prototype.sendToBack = function (obj) {
    this.Modified = true;
    obj.zIndex = this.getMinZIndex() - 1;
}
TTopFlow.prototype.getMaxProcID = function (st) {
    if (!st) {
        st = 'proc';
    }
    var s = "";
    var i, j, u = this.Procs.length;
    for (i = 0;
         i <= u;
         i++) {
        s = st + i;
        for (j = 0;
             j < u;
             j++) {
            if (this.Procs[j].ID == s) {
                break;
            }
        }
        if (j == u) {
            break;
        }
    }
    return s;
}
TTopFlow.prototype.getMaxStepID = function () {
    var s = "";
    var i, j, u = this.Steps.length;
    for (i = 0;
         i <= u;
         i++) {
        s = "step" + i;
        for (j = 0;
             j < u;
             j++) {
            if (this.Steps[j].ID == s) {
                break;
            }
        }
        if (j == u) {
            break;
        }
    }
    return s;
}
TTopFlow.prototype.ProcString = function () {
    var S = "", i;
    for (i = 0;
         i < this.Procs.length;
         i++) {
        S += this.Procs[i];
    }
    return S;
}
TTopFlow.prototype.StepString = function () {
    var S = "", i;
    for (i = 0;
         i < this.Steps.length;
         i++) {
        S += this.Steps[i];
    }
    return S;
}
TTopFlow.prototype.toString = function () {
    return this.ProcString() + this.StepString();
}
TTopFlow.prototype.getOutSteps = function (AProcID) {
    var Steps = [];
    for (var i = 0;
         i < this.Steps.length;
         i++) {
        if (this.Steps[i].FromProc == AProcID) {
            Steps[Steps.length] = this.Steps[i];
        }
    }
    return Steps;
}
TTopFlow.prototype.isExitName = function (val) {
    var isExit = false;
    for (var i = 0;
         i < this.Steps.length;
         i++) {
        if (this.Steps[i].Text.toLowerCase() == val.toLowerCase()) {
            isExit = true;
        }
    }
    for (var i = 0;
         i < this.Procs.length;
         i++) {
        if (this.Procs[i].Text.toLowerCase() == val.toLowerCase()) {
            isExit = true;
        }
    }
    return isExit;
}
TTopFlow.prototype.createNew = function (AName) {
    this.ID = AName;
    this.clear();
    //Proc = new TProc(this, "begin");
    var beginId = getUuid();
    var Proc = new TProc(this, beginId);
    Proc.Text = "开始";
    Proc.ShapeType = "Oval";
    Proc.ProcType = "begin";
    Proc.Width = "40";
    Proc.Height = "40";
    Proc.X = "60";
    Proc.Y = "160";
    this.addProc(Proc);
    //Proc = new TProc(this, "end");
    Proc = new TProc(this, getUuid());
    Proc.Text = "结束";
    Proc.ShapeType = "Oval";
    Proc.ProcType = "end";
    Proc.Width = "40";
    Proc.Height = "40";
    Proc.X = "210";
    Proc.Y = "260";
    this.addProc(Proc);
    var firstId = getUuid();
    //Proc = new TProc(this, "first");
    Proc = new TProc(this, firstId);
    Proc.Text = "第一个节点";
    Proc.ShapeType = "roundrect";
    Proc.ProcType = "first";
    Proc.Executer = "#{starter}";
    Proc.Activity = "";
    Proc.NotifyContent = "";
    Proc.FlowNodeTag = "";
    Proc.X = "180";
    Proc.Y = "160";
    Proc.Width = "100";
    Proc.Height = "40";
    this.addProc(Proc);
    //Step = new TStep(this, "step0");
    Step = new TStep(this, getUuid());
    Step.Text = "流程开始";
    //Step.FromProc = "begin";
    //Step.ToProc = "first";
    Step.FromProc = beginId;
    Step.ToProc = firstId;
    Step.ShapeType = "PolyLine";
    Step.fromRelX = 1;
    Step.fromRelY = 0.5;
    Step.toRelX = 0;
    Step.toRelY = 0.5;
    Step.Points = "75pt,135pt,135pt,135pt";
    Step.Label.X = "120";
    Step.Label.Y = "190"
    Step.Label.Width = "60"
    this.addStep(Step);
}
TTopFlow.prototype.loadFromXML = function (AFileName) {
    this.clear();
    this.FileName = AFileName;
    var xmlDoc = new ActiveXObject('MSXML2.DOMDocument');
    xmlDoc.async = false;
    var flag = xmlDoc.load('data/' + AFileName + '.xml');
    if (!flag) {
        alert('文件[' + AFileName + '载入失败！');
        this.createNew(AFileName);
        return false;
    }
    var xmlRoot = xmlDoc.documentElement.getElementsByTagName("Flow").item(0);
    this.Text = xmlRoot.getAttribute("text");
    this.Password = xmlRoot.getAttribute("password");
    this.ID = xmlRoot.getAttribute("id");
    this.FormID = xmlRoot.getAttribute("formid");
    var Procs = xmlRoot.getElementsByTagName("Procs").item(0);
    var id, oNode, Prop;
    for (i = 0;
         i < Procs.childNodes.length;
         i++) {
        var Proc = Procs.childNodes.item(i);
        Prop = Proc.getElementsByTagName("BaseProperties").item(0);
        id = Prop.getAttribute("id");
        oNode = new TProc(this, id);
        oNode.Text = Prop.getAttribute("text");
        oNode.ProcType = Prop.getAttribute("ProcType");
        oNode.SelectExit = Prop.getAttribute("SelectExit");
        oNode.AllowAgent = Prop.getAttribute("AllowAgent");
        oNode.NotifyInstanceCreator = Prop.getAttribute("NotifyInstanceCreator");
        oNode.InstanceReturnMode = Prop.getAttribute("InstanceReturnMode");
        oNode.SkipHandler = Prop.getAttribute("SkipHandler");
        oNode.WarnType = Prop.getAttribute("WarnType");
        oNode.HandlerFetchCount = Prop.getAttribute("HandlerFetchCount");
        oNode.HandlerFetchType = Prop.getAttribute("HandlerFetchType");
        oNode.Executer = Prop.getAttribute("Executer");
        oNode.HandlerOnFlowNode = Prop.getAttribute("HandlerOnFlowNode");
        oNode.UserPropertyOnEntity = Prop.getAttribute("UserPropertyOnEntity");
        oNode.FlowNodeTag = Prop.getAttribute("FlowNodeTag");
        oNode.EntityStatusTag = Prop.getAttribute("EntityStatusTag");
        oNode.Activity = Prop.getAttribute("Activity");
        oNode.FlowNodeAction = Prop.getAttribute("FlowNodeAction");
        oNode.Aidancer = Prop.getAttribute("Aidancer");
        oNode.TimeLimit = Prop.getAttribute("TimeLimit");
        Prop = Proc.getElementsByTagName("VMLProperties").item(0);
        oNode.ShapeType = Prop.getAttribute("shapetype");
        oNode.Width = Prop.getAttribute("width");
        oNode.Height = Prop.getAttribute("height");
        oNode.X = Prop.getAttribute("x");
        oNode.Y = Prop.getAttribute("y");
        oNode.TextWeight = Prop.getAttribute("textWeight");
        oNode.StrokeWeight = Prop.getAttribute("strokeWeight");
        oNode.zIndex = Prop.getAttribute("zIndex");
        oNode.SameCredit = Prop.getAttribute("SameCredit");
        if (oNode.zIndex == '') {
            oNode.zIndex = this.getMinZIndex() - 1;
        }
        this.addProc(oNode);
    }
    var Steps = xmlRoot.getElementsByTagName("Steps").item(0);
    for (i = 0;
         i < Steps.childNodes.length;
         i++) {
        var Step = Steps.childNodes.item(i);
        Prop = Step.getElementsByTagName("BaseProperties").item(0);
        id = Prop.getAttribute("id");
        oNode = new TStep(this, id);
        oNode.Text = Prop.getAttribute("text");
        oNode.FromProc = Prop.getAttribute("from");
        oNode.ToProc = Prop.getAttribute("to");
        oNode.Cond = Prop.getAttribute("Cond");
        oNode.Cond = oNode.Cond.replace(/\'/g, '"')
        oNode.Desc = Prop.getAttribute("Desc");
        Prop = Step.getElementsByTagName("VMLProperties").item(0);
        oNode.Points = Prop.getAttribute("points");
        oNode.fromRelX = Prop.getAttribute("fromRelX");
        oNode.fromRelY = Prop.getAttribute("fromRelY");
        oNode.toRelX = Prop.getAttribute("toRelX");
        oNode.toRelY = Prop.getAttribute("toRelY");
        oNode.ShapeType = Prop.getAttribute("shapetype");
        oNode.StartArrow = Prop.getAttribute("startArrow");
        oNode.EndArrow = Prop.getAttribute("endArrow");
        oNode.StrokeWeight = Prop.getAttribute("strokeWeight");
        oNode.zIndex = Prop.getAttribute("zIndex");
        var LabProp = Step.getElementsByTagName("LabelProperties").item(0);
        if (LabProp) {
            id = LabProp.getAttribute("id");
            oLabel = new TLabel(id);
            oLabel.Text = Prop.getAttribute("text");
            oLabel.Desc = Prop.getAttribute("desc");
            oLabel.X = LabProp.getAttribute("x");
            oLabel.Y = LabProp.getAttribute("y");
            oLabel.Width = LabProp.getAttribute("width");
            oLabel.Height = LabProp.getAttribute("height");
            oNode.Label = oLabel;
        }
        if (oNode.zIndex == '') {
            oNode.zIndex = this.getMinZIndex() - 1;
        }
        this.addStep(oNode);
    }
    this.Modified = false;
    return true;
}

// 读取
TTopFlow.prototype.loadXmlConfig = function (AFileName, xml) {
    this.clear();
    this.FileName = AFileName;
    var xmlDoc = new ActiveXObject('MSXML2.DOMDocument');
    xmlDoc.async = false;

    xmlDoc.loadXML(xml);

    var xmlRoot = xmlDoc.documentElement.getElementsByTagName("Flow").item(0);
    this.Text = xmlRoot.getAttribute("text");
    //this.Password = xmlRoot.getAttribute("password");
    //this.ID = xmlRoot.getAttribute("id");
    this.FormID = xmlRoot.getAttribute("formid");
    var Procs = xmlRoot.getElementsByTagName("Procs").item(0);
    var id, oNode, Prop;

    for (i = 0;
         i < Procs.childNodes.length;
         i++) {
        var Proc = Procs.childNodes.item(i);
        Prop = Proc.getElementsByTagName("BaseProperties").item(0);
        id = Prop.getAttribute("id");
        oNode = new TProc(this, id);
        oNode.Text = Prop.getAttribute("text");
        oNode.ProcType = Prop.getAttribute("ProcType");
        oNode.SelectExit = Prop.getAttribute("SelectExit");
        oNode.SkipHandler = Prop.getAttribute("SkipHandler");
        oNode.AllowAgent = Prop.getAttribute("AllowAgent");
        oNode.NotifyInstanceCreator = Prop.getAttribute("NotifyInstanceCreator");
        oNode.InstanceReturnMode = Prop.getAttribute("InstanceReturnMode");
        oNode.SuspendInstance = Prop.getAttribute("SuspendInstance");
        oNode.SuspendDescription = Prop.getAttribute("SuspendDescription");
        oNode.WarnType = Prop.getAttribute("WarnType");
        oNode.Executer = Prop.getAttribute("Executer");
        oNode.HandlerOnFlowNode = Prop.getAttribute("HandlerOnFlowNode");
        oNode.UserPropertyOnEntity = Prop.getAttribute("UserPropertyOnEntity");
        oNode.Activity = Prop.getAttribute("Activity");
        oNode.NotifyContent = Prop.getAttribute("NotifyContent");
        oNode.FlowNodeTag = Prop.getAttribute("FlowNodeTag");
        oNode.EntityStatusTag = Prop.getAttribute("EntityStatusTag");
        oNode.OrganizationId = Prop.getAttribute("OrganizationId");
        oNode.RoleId = Prop.getAttribute("RoleId");
        oNode.UseRootNode = Prop.getAttribute("UseRootNode");
        oNode.OrganizationProperty = Prop.getAttribute("OrganizationProperty");
        oNode.RoleProperty = Prop.getAttribute("RoleProperty");
        oNode.HandlerFetchCount = Prop.getAttribute("HandlerFetchCount");
        oNode.HandlerFetchType = Prop.getAttribute("HandlerFetchType");
        oNode.FlowNodeAction = Prop.getAttribute("FlowNodeAction");
        oNode.Aidancer = Prop.getAttribute("Aidancer");
        oNode.TimeLimit = Prop.getAttribute("TimeLimit");
        Prop = Proc.getElementsByTagName("VMLProperties").item(0);
        oNode.ShapeType = Prop.getAttribute("shapetype");
        oNode.Width = Prop.getAttribute("width");
        oNode.Height = Prop.getAttribute("height");
        oNode.X = Prop.getAttribute("x");
        oNode.Y = Prop.getAttribute("y");
        oNode.TextWeight = Prop.getAttribute("textWeight");
        oNode.StrokeWeight = Prop.getAttribute("strokeWeight");
        oNode.zIndex = Prop.getAttribute("zIndex");
        oNode.SameCredit = Prop.getAttribute("SameCredit");
        if (oNode.zIndex == '') {
            oNode.zIndex = this.getMinZIndex() - 1;
        }
        this.addProc(oNode);
    }

    var Steps = xmlRoot.getElementsByTagName("Steps").item(0);
    for (i = 0;
         i < Steps.childNodes.length;
         i++) {
        var Step = Steps.childNodes.item(i);
        Prop = Step.getElementsByTagName("BaseProperties").item(0);
        id = Prop.getAttribute("id");
        oNode = new TStep(this, id);
        oNode.Uuid = Prop.getAttribute("uuid");
        oNode.Text = Prop.getAttribute("text");
        oNode.FromProc = Prop.getAttribute("from");
        oNode.ToProc = Prop.getAttribute("to");
        oNode.Cond = Prop.getAttribute("Cond");
        oNode.Event = Prop.getAttribute("Event");
        oNode.RamusTag = Prop.getAttribute("RamusTag");
        oNode.StatusTag = Prop.getAttribute("StatusTag");
        oNode.Cond = oNode.Cond.replace(/\'/g, '"')
        oNode.Desc = Prop.getAttribute("Desc");
        Prop = Step.getElementsByTagName("VMLProperties").item(0);
        oNode.Points = Prop.getAttribute("points");
        oNode.fromRelX = Prop.getAttribute("fromRelX");
        oNode.fromRelY = Prop.getAttribute("fromRelY");
        oNode.toRelX = Prop.getAttribute("toRelX");
        oNode.toRelY = Prop.getAttribute("toRelY");
        oNode.ShapeType = Prop.getAttribute("shapetype");
        oNode.StartArrow = Prop.getAttribute("startArrow");
        oNode.EndArrow = Prop.getAttribute("endArrow");
        oNode.StrokeWeight = Prop.getAttribute("strokeWeight");
        oNode.zIndex = Prop.getAttribute("zIndex");
        var LabProp = Step.getElementsByTagName("LabelProperties").item(0);
        if (LabProp) {
            id = LabProp.getAttribute("id");
            oLabel = new TLabel(id);
            oLabel.Text = Prop.getAttribute("text");
            oLabel.Desc = Prop.getAttribute("desc");
            oLabel.X = LabProp.getAttribute("x");
            oLabel.Y = LabProp.getAttribute("y");
            oLabel.Width = LabProp.getAttribute("width");
            oLabel.Height = LabProp.getAttribute("height");
            oNode.Label = oLabel;
        }
        if (oNode.zIndex == '') {
            oNode.zIndex = this.getMinZIndex() - 1;
        }

        this.addStep(oNode);
    }
    this.Modified = false;
    return true;
}

// 保存
TTopFlow.prototype.getConfigXml = function () {
    var xmlDoc = new ActiveXObject('MSXML2.DOMDocument');
    xmlDoc.async = false;
    xmlDoc.loadXML('<?xml version="1.0" encoding="utf-8"?><WorkflowConfig/>');
    var xmlRoot = xmlDoc.documentElement;
    var xmlNodeGrp, xmlNode, xmlNode2, xmlFlow;
    xmlFlow = xmlDoc.createNode(1, "Flow", "");
    xmlRoot.appendChild(xmlFlow);
    xmlFlow.setAttribute("id", this.ID);
    xmlFlow.setAttribute("formid", this.FormID);
    xmlNodeGrp = xmlDoc.createNode(1, "Procs", "");
    xmlFlow.appendChild(xmlNodeGrp);
    for (var i = 0;
         i < this.Procs.length;
         i++) {
        Proc = this.Procs[i];
        xmlNode = xmlDoc.createNode(1, "Proc", "");
        xmlNode2 = xmlDoc.createNode(1, "BaseProperties", "");
        xmlNode2.setAttribute("id", Proc.ID);
        xmlNode2.setAttribute("text", Proc.Text);
        xmlNode2.setAttribute("ProcType", Proc.ProcType);
        xmlNode2.setAttribute("SelectExit", Proc.SelectExit);
        xmlNode2.setAttribute("WarnType", Proc.WarnType);
        xmlNode2.setAttribute("TimeLimit", Proc.TimeLimit);
        xmlNode2.setAttribute("Executer", Proc.Executer);
        if (Proc.SkipHandler != null) {
            xmlNode2.setAttribute("SkipHandler", Proc.SkipHandler);
        }
        if (Proc.AllowAgent != null) {
            xmlNode2.setAttribute("AllowAgent", Proc.AllowAgent);
        }
        if (Proc.NotifyInstanceCreator != null) {
            xmlNode2.setAttribute("NotifyInstanceCreator", Proc.NotifyInstanceCreator);
        }
        if (Proc.InstanceReturnMode != null) {
            xmlNode2.setAttribute("InstanceReturnMode", Proc.InstanceReturnMode);
        }
        if (Proc.SuspendInstance != null) {
            xmlNode2.setAttribute("SuspendInstance", Proc.SuspendInstance);
        }
        if (Proc.SuspendDescription != null) {
            xmlNode2.setAttribute("SuspendDescription", Proc.SuspendDescription);
        }
        if (Proc.HandlerOnFlowNode != null) {
            xmlNode2.setAttribute("HandlerOnFlowNode", Proc.HandlerOnFlowNode);
        }
        if (Proc.UserPropertyOnEntity != null) {
            xmlNode2.setAttribute("UserPropertyOnEntity", Proc.UserPropertyOnEntity);
        }
        if (Proc.Activity != null) {
            xmlNode2.setAttribute("Activity", Proc.Activity);
        }
        if (Proc.NotifyContent != null) {
            xmlNode2.setAttribute("NotifyContent", Proc.NotifyContent);
        }
        if (Proc.FlowNodeTag != null) {
            xmlNode2.setAttribute("FlowNodeTag", Proc.FlowNodeTag);
        }
        if (Proc.EntityStatusTag != null) {
            xmlNode2.setAttribute("EntityStatusTag", Proc.EntityStatusTag);
        }
        if (Proc.OrganizationId != null) {
            xmlNode2.setAttribute("OrganizationId", Proc.OrganizationId);
        }
        if (Proc.RoleId != null) {
            xmlNode2.setAttribute("RoleId", Proc.RoleId);
        }
        if (Proc.UseRootNode != null) {
            xmlNode2.setAttribute("UseRootNode", Proc.UseRootNode);
        }
        if (Proc.OrganizationProperty != null) {
            xmlNode2.setAttribute("OrganizationProperty", Proc.OrganizationProperty);
        }
        if (Proc.RoleProperty != null) {
            xmlNode2.setAttribute("RoleProperty", Proc.RoleProperty);
        }
        if (Proc.HandlerFetchCount != null) {
            xmlNode2.setAttribute("HandlerFetchCount", Proc.HandlerFetchCount);
        }
        if (Proc.HandlerFetchType != null) {
            xmlNode2.setAttribute("HandlerFetchType", Proc.HandlerFetchType);
        }

        if (Proc.FlowNodeAction != null) {
            xmlNode2.setAttribute("FlowNodeAction", Proc.FlowNodeAction);
        }
        xmlNode2.setAttribute("Aidancer", Proc.Aidancer);
        xmlNode.appendChild(xmlNode2);
        xmlNode2 = xmlDoc.createNode(1, "VMLProperties", "");
        xmlNode2.setAttribute("shapetype", Proc.ShapeType);
        xmlNode2.setAttribute("width", Proc.Width);
        xmlNode2.setAttribute("height", Proc.Height);
        xmlNode2.setAttribute("x", Proc.X);
        xmlNode2.setAttribute("y", Proc.Y);
        xmlNode2.setAttribute("textWeight", Proc.TextWeight);
        xmlNode2.setAttribute("strokeWeight", Proc.StrokeWeight);
        xmlNode2.setAttribute("zIndex", Proc.zIndex);
        xmlNode.appendChild(xmlNode2);
        xmlNodeGrp.appendChild(xmlNode);
    }
    xmlNodeGrp = xmlDoc.createNode(1, "Steps", "");
    xmlFlow.appendChild(xmlNodeGrp);
    for (i = 0;
         i < this.Steps.length;
         i++) {
        Step = this.Steps[i];
        xmlNode = xmlDoc.createNode(1, "Step", "");
        xmlNode2 = xmlDoc.createNode(1, "BaseProperties", "");
        xmlNode2.setAttribute("id", Step.ID);
        xmlNode2.setAttribute("text", Step.Text);
        xmlNode2.setAttribute("from", Step.FromProc);
        xmlNode2.setAttribute("to", Step.ToProc);
        xmlNode2.setAttribute("Cond", Step.Cond);
        if (Step.Event != null) {
            xmlNode2.setAttribute("Event", Step.Event);
        }
        if (Step.RamusTag != null) {
            xmlNode2.setAttribute("RamusTag", Step.RamusTag);
        }
        if (Step.StatusTag != null) {
            xmlNode2.setAttribute("StatusTag", Step.StatusTag);
        }
        xmlNode2.setAttribute("Desc", Step.Desc);
        xmlNode.appendChild(xmlNode2);
        xmlNode2 = xmlDoc.createNode(1, "VMLProperties", "");
        xmlNode2.setAttribute("points", Step.Points);
        xmlNode2.setAttribute("fromRelX", Step.fromRelX);
        xmlNode2.setAttribute("fromRelY", Step.fromRelY);
        xmlNode2.setAttribute("toRelX", Step.toRelX);
        xmlNode2.setAttribute("toRelY", Step.toRelY);
        xmlNode2.setAttribute("shapetype", Step.ShapeType);
        xmlNode2.setAttribute("startArrow", Step.StartArrow);
        xmlNode2.setAttribute("endArrow", Step.EndArrow);
        xmlNode2.setAttribute("strokeWeight", Step.StrokeWeight);
        xmlNode2.setAttribute("zIndex", Step.zIndex);
        xmlNode.appendChild(xmlNode2);
        xmlNode2 = xmlDoc.createNode(1, "LabelProperties", "");
        xmlNode2.setAttribute("id", Step.Label.ID);
        xmlNode2.setAttribute("width", Step.Label.Width);
        xmlNode2.setAttribute("height", Step.Label.Height);
        xmlNode2.setAttribute("x", Step.Label.X);
        xmlNode2.setAttribute("y", Step.Label.Y);
        xmlNode.appendChild(xmlNode2);
        xmlNodeGrp.appendChild(xmlNode);
    }
    return xmlDoc.xml;
}

