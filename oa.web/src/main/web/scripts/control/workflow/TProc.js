function TProc(AFlow, id, st) {
    var idPerfix = '';
    this.ObjType = "Proc";
    this.Flow = AFlow;
    this.ID = id;
    this.ShapeType = st;
    if (st) {
        this.ProcType = this.getProcType();
    }
    idPerfix = this.ProcType;
    if (this.ProcType == 'andjoin') {
        idPerfix = 'join';
    }
    if (this.ProcType != 'andjoin' && this.ProcType != 'fork') {
        idPerfix = '';
    }
    if (this.ID == undefined) {
        //this.ID = this.Flow.getMaxProcID(idPerfix);
        this.ID = getUuid(); // wayne
    }
    //this.Text = "" + this.ID;
    this.Text = "" + this.Flow.getMaxProcID(idPerfix);
    if (this.ProcType == 'andjoin') {
        this.Text = this.Text.replace('join', '汇聚');
    }
    else if (this.ProcType == 'fork') {
        this.Text = this.Text.replace('fork', '分支');
    }
    else {
        this.Text = this.Text.replace('proc', '节点');
    }
    this.Width = "50";
    this.Height = "50";
    this.X = "50";
    this.Y = "50";
    this.TextWeight = "9pt";
    this.StrokeWeight = "1";
    this.zIndex = 1;
    this.InnerObject = null;
    this.MoveType = "";
    this.actFlag = "";
    this.WaitTime = "";
    this.SelectExit = "0";
    this.SkipHandler = "0";
    this.AllowAgent = "0";
    this.NotifyInstanceCreator = "0";
    this.InstanceReturnMode = "0";
    this.SuspendInstance = "0";
    this.SuspendDescription = '';
    this.SameCredit = "1";
    this.WarnType = '';
    this.Executer = '';
    this.HandlerOnFlowNode = '';
    this.UserPropertyOnEntity = '';
    this.Aidancer = '';
    this.TimeLimit = '';
    this.Activity = '';
    this.NotifyContent = '',
        this.FlowNodeTag = '';
    this.EntityStatusTag = '';
    this.FlowNodeAction = '';
    this.OrganizationId = '';
    this.RoleId = '';
    this.UseRootNode = '';
    this.OrganizationProperty = '';
    this.RoleProperty = '';
    this.HandlerFetchCount = '';
    this.HandlerFetchType = '';

}
TProc.prototype.getInnerObject = function () {
    if (this.InnerObject == null) {
        this.InnerObject = document.all(this.ID);
    }
    return this.InnerObject;
}
TProc.prototype.setFocus = function () {
    this.getInnerObject.StrokeColor = this.Flow.Config.ProcFocusedStrokeColor;
}
TProc.prototype.lostFocus = function () {
    this.getInnerObject.StrokeColor = (this.ProcType == "NormalProc") ? this.Flow.Config.ProcColor : this.Flow.Config._ProcColor;
}
TProc.prototype.doClick = function () {
    this.Flow.selectObject(this.ID, "Proc");
}
TProc.prototype.mouseDown = function () {
    var rightSide = (parseInt(this.X) + parseInt(this.Width) - event.x <= 2);
    var bottomSide = (parseInt(this.Y) + parseInt(this.Height) - event.y <= 2);
    if (rightSide && bottomSide) {
        this.MoveType = "nw";
    }
    else if (rightSide) {
        this.MoveType = "e";
    }
    else if (bottomSide) {
        this.MoveType = "n";
    }
    else {
        this.MoveType = "m";
    }
    this.getInnerObject.setCapture();
    switch (this.MoveType) {
        case"m":
            this.CurrentX = event.x - this.InnerObject.offsetLeft;
            this.CurrentY = event.y - this.InnerObject.offsetTop;
            break;
        case"front":
        case"back":
            if (_TOOLTYPE == "front") {
                this.Flow.brintToFront(this);
            }
            else {
                this.Flow.sendToBack(this);
            }
            this.getInnerObject.style.zIndex = this.zIndex;
            break;
    }
}
TProc.prototype.mouseMove = function () {
    switch (this.MoveType) {
        case"m":
            this.X = event.x - this.CurrentX;
            this.Y = event.y - this.CurrentY;
            if (this.X < 0) {
                this.X = 0;
            }
            if (this.Y < 30) {
                this.Y = 30;
            }
            this.InnerObject.style.left = this.X;
            this.InnerObject.style.top = this.Y;
            break;
        case"n":
            this.Height = event.y - this.Y;
            if (this.Height < 30) {
                this.Height = 30;
            }
            this.InnerObject.style.height = this.Height;
            break;
        case"e":
            this.Width = event.x - this.X;
            if (this.Width < 30) {
                this.Width = 30;
            }
            this.InnerObject.style.width = this.Width;
            break;
        case"nw":
            this.Width = event.x - this.X;
            this.Height = event.y - this.Y;
            if (this.Width < 30) {
                this.Width = 30;
            }
            if (this.Height < 30) {
                this.Height = 30;
            }
            this.InnerObject.style.width = this.Width;
            this.InnerObject.style.height = this.Height;
            break;
        default:
            var rightSide = (parseInt(this.X) + parseInt(this.Width) - event.x <= 2);
            var bottomSide = (parseInt(this.Y) + parseInt(this.Height) - event.y <= 2);
            if (rightSide && bottomSide) {
                this.getInnerObject.style.cursor = "NW-resize";
            }
            else if (rightSide) {
                this.getInnerObject.style.cursor = "E-resize";
            }
            else if (bottomSide) {
                this.getInnerObject.style.cursor = "N-resize";
            }
            else {
                this.getInnerObject.style.cursor = "hand";
            }
            break;
    }
}
TProc.prototype.mouseUp = function () {
    if (this.MoveType != "") {
        this.getInnerObject.releaseCapture();
        if (this.MoveType == "nw") {
            if (parseInt(this.InnerObject.style.top) < -10) {
                alert("对象上边界超出，将自动调整.");
                this.InnerObject.style.top = 30;
            }
            if (parseInt(this.InnerObject.style.left) < -10) {
                alert("对象左边界超出，将自动调整.");
                this.InnerObject.style.left = 30;
            }
        }
    }
    this.MoveType = "";
}
TProc.prototype.clone = function (AProc) {
    this.ID = AProc.ID;
    this.Text = AProc.Text;
    this.ShapeType = AProc.ShapeType
    this.ProcType = AProc.ProcType;
    this.Width = AProc.Width;
    this.Height = AProc.Height;
    this.X = AProc.X;
    this.Y = AProc.Y;
    this.TextWeight = AProc.TextWeight;
    this.StrokeWeight = AProc.StrokeWeight;
    this.zIndex = AProc.zIndex;
    this.InnerObject = null;
    this.MoveType = "";
    this.WarnType = AProc.WarnType;
    this.SelectExit = AProc.SelectExit;
    this.AllowAgent = AProc.AllowAgent;
    this.NotifyInstanceCreator = AProc.NotifyInstanceCreator;
    this.InstanceReturnMode = AProc.InstanceReturnMode;
    this.SkipHandler = AProc.SkipHandler;
    this.SuspendInstance = AProc.SuspendInstance;
    this.SameCredit = AProc.SameCredit;
    this.Executer = AProc.Executer;
    this.SuspendDescription = AProc.SuspendDescription;
    this.HandlerOnFlowNode = AProc.HandlerOnFlowNode;
    this.UserPropertyOnEntity = AProc.UserPropertyOnEntity;
    this.FlowNodeTag = AProc.FlowNodeTag;
    this.EntityStatusTag = AProc.EntityStatusTag;
    this.OrganizationId = AProc.OrganizationId;
    this.RoleId = AProc.RoleId;
    this.UseRootNode = AProc.UseRootNode;
    this.OrganizationProperty = AProc.OrganizationProperty;
    this.RoleProperty = AProc.RoleProperty;
    this.HandlerFetchCount = AProc.HandlerFetchCount;
    this.HandlerFetchType = AProc.HandlerFetchType;
    this.Activity = AProc.Activity;
    this.NotifyContent = AProc.NotifyContent;
    this.FlowNodeAction = AProc.FlowNodeAction;
    this.Aidancer = AProc.Aidancer;
    this.TimeLimit = AProc.TimeLimit;
}
TProc.prototype.setPropValue = function (AProp, AValue) {
    switch (AProp) {
        case"ID":
            var oldID = this.ID;
            if (oldID == AValue) {
                return true;
            }
            if (this.Flow.IDExists(AValue)) {
                alert("编号[" + AValue + "]已经存在！");
                return false;
            }
            this.InnerObject.all(oldID + "Text").id = AValue + "Text";
            this.ID = AValue;
            this.InnerObject.id = AValue;
            this.Flow.changeProcID(oldID, AValue);
            break;
        case"X":
            this.X = AValue;
            this.InnerObject.style.left = AValue;
            break;
        case"Y":
            this.Y = AValue;
            this.InnerObject.style.top = AValue;
            break;
        case"Width":
            this.Width = AValue;
            this.InnerObject.style.width = AValue;
            break;
        case"Height":
            this.Height = AValue;
            this.InnerObject.style.height = AValue;
            break;
    }
}
TProc.prototype.toString = function () {
    var cl = this.Flow.Config;
    var nStockeColor, nTextColor;
    if (this.ProcType == 'begin' || this.ProcType == 'end') {
        nTextColor = cl._ProcTextColor;
        nStrokeColor = cl._ProcColor;
    }
    else {
        nTextColor = cl.ProcTextColor;
        nStrokeColor = cl.ProcColor;
    }
    var arrVal = new Array();
    arrVal["id"] = this.ID;
    arrVal["title"] = this.ID;
    arrVal["sc"] = nStrokeColor;
    arrVal["st"] = this.ProcType;
    arrVal["l"] = this.X;
    arrVal["t"] = this.Y;
    arrVal["w"] = this.Width;
    arrVal["h"] = this.Height;
    arrVal["z"] = this.zIndex;
    arrVal["sw"] = this.StrokeWeight;
    arrVal["fsc"] = cl.ProcFocusedStrokeColor;
    arrVal["shadowenable"] = cl.IsProcShadow;
    arrVal["shadowcolor"] = cl.ProcShadowColor;
    arrVal["3denable"] = cl.IsProc3D;
    arrVal["3ddepth"] = cl.Proc3DDepth;
    arrVal["sc1"] = cl.ProcColor1;
    arrVal["sc2"] = cl.ProcColor2;
    arrVal["tc"] = nTextColor;
    arrVal["fs"] = this.TextWeight;
    arrVal["text"] = this.Text;
    arrVal["af"] = this.actFlag;
    arrVal["wt"] = this.WaitTime;
    arrVal["ist"] = this.SelectExit;
    arrVal["allowAgent"] = this.AllowAgent;
    arrVal["isc"] = this.SameCredit;
    return stuffShape(getShapeVal(this.ShapeType), arrVal);
}
TProc.prototype.getProcType = function () {
    switch (this.ShapeType) {
        case"rect":
            return'andjoin';
            break;
        case"roundrect":
            return'andsign';
            break;
        case"diamond":
            return'fork';
            break;
        default:
            break;
    }
}