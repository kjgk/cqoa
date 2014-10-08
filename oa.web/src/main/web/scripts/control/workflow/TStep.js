function TStep(AFlow, id) {
    this.ObjType = "Step";
    this.Flow = AFlow;
    this.ID = id;
    if (this.ID == undefined) {
        // wayne
        //this.ID = this.Flow.getMaxStepID();
        this.ID = getUuid();
    }
    //this.Text = "" + this.ID;
    this.Text = "" + this.Flow.getMaxStepID();
    this.ShapeType = "Line";
    this.FromProc = "";
    this.ToProc = "";
    this.Points = "";
    this.StartArrow = "none";
    this.EndArrow = "Classic";
    this.TextWeight = "9pt";
    this.StrokeWeight = "1";
    this.zIndex = 0;
    this.InnerObject = null;
    this.fromRelX = 0;
    this.fromRelY = 0;
    this.toRelX = 0;
    this.toRelY = 0;
    this.Label = new TLabel('lab' + this.ID);
    this.Cond = '';
    this.Event = '';
    this.Desc = '';
    this.RamusTag = '';
    this.StatusTag = '';
}
TStep.prototype.clone = function (AStep) {
    this.ID = AStep.ID;
    this.Text = AStep.Text;
    this.ShapeType = AStep.ShapeType;
    this.FromProc = AStep.FromProc;
    this.ToProc = AStep.ToProc;
    this.Points = AStep.Points;
    this.StartArrow = AStep.StartArrow;
    this.EndArrow = AStep.EndArrow;
    this.TextWeight = AStep.TextWeight;
    this.StrokeWeight = AStep.StrokeWeight;
    this.zIndex = AStep.zIndex;
    this.Points = AStep.Points;
    this.fromRelX = AStep.fromRelX;
    this.fromRelY = AStep.fromRelY;
    this.toRelX = AStep.toRelX;
    this.toRelY = AStep.toRelY;
    this.Label = AStep.Label;
    this.Cond = AStep.Cond;
    this.Event = AStep.Event;
    this.Desc = AStep.Desc;
    this.RamusTag = AStep.RamusTag;
    this.StatusTag = AStep.StatusTag;
}
TStep.prototype.setPropValue = function (AProp, AValue) {
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
            this.InnerObject.all(oldID + "Arrow").id = AValue + "Arrow";
            this.ID = AValue;
            this.InnerObject.id = AValue;
            break;
        case"Points":
            this.Points = AValue;
            break;
        case"FromProc":
            this.FromProc = AValue;
            break;
        case"ToProc":
            this.ToProc = AValue;
            break;
    }
}
TStep.prototype.toString = function () {
    var StepHTML = '';
    var cl = this.Flow.Config;
    var arrVal = new Array();
    arrVal["id"] = this.ID;
    arrVal["title"] = this.ID + ':' + this.Text;
    arrVal["sc"] = cl.StepColor;
    arrVal["pt"] = this.getPath();
    arrVal["z"] = this.zIndex;
    arrVal["sw"] = this.StrokeWeight;
    arrVal["fsc"] = cl.StepFocusedStrokeColor;
    arrVal["sa"] = this.StartArrow;
    arrVal["ea"] = this.EndArrow;
    arrVal["cond"] = this.Cond;
    arrVal["text"] = this.Text;
    arrVal["lid"] = this.Label.ID;
    arrVal["x"] = this.Label.X;
    arrVal["y"] = this.Label.Y;
    arrVal["w"] = this.Label.Width;
    arrVal["h"] = this.Label.Height;
    arrVal["desc"] = this.Desc;
    return stuffShape(getShapeVal(this.ShapeType), arrVal);
}
TStep.prototype.getInnerObject = function () {
    if (this.InnerObject == null) {
        this.InnerObject = document.all(this.ID);
    }
    return this.InnerObject;
}
TStep.prototype.setFocus = function () {
    this.getInnerObject.StrokeColor = this.Flow.Config.StepFocusedStrokeColor;
}
TStep.prototype.lostFocus = function () {
    this.getInnerObject.StrokeColor = this.Flow.Config.StepColor;
}
TStep.prototype.doClick = function () {
    this.Flow.selectObject(this.ID, "Step");
}
TStep.prototype.getPath = function () {
    if (this.Points != null && this.Points != "") {
        return this.Points;
    }
    var fromProc = document.getElementById(this.FromProc), toProc = document.getElementById(this.ToProc);
    if (fromProc == null || toProc == null) {
        return'';
    }
    var fromW = parseInt(fromProc.style.width);
    var fromH = parseInt(fromProc.style.height);
    var toW = parseInt(toProc.style.width);
    var toH = parseInt(toProc.style.height);
    var fromX = parseInt(fromProc.style.left);
    var fromY = parseInt(fromProc.style.top);
    var toX = parseInt(toProc.style.left);
    var toY = parseInt(toProc.style.top);
    if (this.FromProc == this.ToProc) {
        return this.getSelfPath(fromX, fromY, fromW, fromH);
    }
    if (ifRepeatProc(fromX, fromY, fromW, fromH, toX, toY, toW, toH)) {
        return"";
    }
    else if (this.ShapeType == "PolyLine") {
        return this.getLinePath(fromX, fromY, fromW, fromH, toX, toY, toW, toH);
    }
    else {
        return this.Points;
    }
}
TStep.prototype.reGetPath = function () {
    var fromProc = document.getElementById(this.FromProc), toProc = document.getElementById(this.ToProc);
    if (fromProc == null || toProc == null) {
        return'';
    }
    var fromW = parseInt(fromProc.style.width);
    var fromH = parseInt(fromProc.style.height);
    var toW = parseInt(toProc.style.width);
    var toH = parseInt(toProc.style.height);
    var fromX = parseInt(fromProc.style.left);
    var fromY = parseInt(fromProc.style.top);
    var toX = parseInt(toProc.style.left);
    var toY = parseInt(toProc.style.top);
    if (this.FromProc == this.ToProc) {
        return this.getSelfPath(fromX, fromY, fromW, fromH);
    }
    if (ifRepeatProc(fromX, fromY, fromW, fromH, toX, toY, toW, toH)) {
        return"";
    }
    else if (this.ShapeType == "PolyLine") {
        return this.getLinePath(fromX, fromY, fromW, fromH, toX, toY, toW, toH);
    }
    else {
        return this.Points;
    }
}
TStep.prototype.getSelfPath = function (ProcX, ProcY, ProcW, ProcH) {
    var constLength = 10;
    point0X = ProcX + ProcW - constLength;
    point0Y = ProcY + ProcH;
    point1X = point0X;
    point1Y = point0Y + constLength;
    point2X = ProcX + ProcW + constLength;
    point2Y = point1Y;
    point3X = point2X;
    point3Y = point0Y - constLength;
    point4X = ProcX + ProcW;
    point4Y = point3Y;
    return point0X + ',' + point0Y + ' ' + point1X + ',' + point1Y + ' ' + point2X + ',' + point2Y + ' ' + point3X + ',' + point3Y + ' ' + point4X + ',' + point4Y;
}
TStep.prototype.getLinePath = function (fromProcX, fromProcY, fromProcW, fromProcH, toProcX, toProcY, toProcW, toProcH) {
    var fromX, fromY, toX, toY, fromRelX, fromRelY, toRelX, toRelY;
    if (fromProcY + fromProcH < toProcY) {
        if (fromProcX + fromProcW < toProcX) {
            fromX = fromProcX + fromProcW;
            fromY = fromProcY + fromProcH;
            toX = toProcX;
            toY = toProcY;
            fromRelX = 1;
            fromRelY = 1;
            toRelX = 0;
            toRelY = 0;
        }
        else if (fromProcX > toProcX + toProcW) {
            fromX = fromProcX;
            fromY = fromProcY + fromProcH;
            toX = toProcX + toProcW;
            toY = toProcY;
            fromRelX = 0;
            fromRelY = 1;
            toRelX = 1;
            toRelY = 0;
        }
        else {
            fromX = fromProcX + parseInt(fromProcW / 2);
            fromY = fromProcY + fromProcH;
            toX = toProcX + parseInt(toProcW / 2);
            toY = toProcY;
            fromRelX = 0.5;
            fromRelY = 1;
            toRelX = 0.5;
            toRelY = 0;
        }
    }
    else if (fromProcY > toProcY + toProcH) {
        if (fromProcX + fromProcW < toProcX) {
            fromX = fromProcX + fromProcW;
            fromY = fromProcY;
            toX = toProcX;
            toY = toProcY + toProcH;
            fromRelX = 1;
            fromRelY = 0;
            toRelX = 0;
            toRelY = 1;
        }
        else if (fromProcX > toProcX + toProcW) {
            fromX = fromProcX;
            fromY = fromProcY;
            toX = toProcX + toProcW;
            toY = toProcY + toProcH;
            fromRelX = 0;
            fromRelY = 0;
            toRelX = 1;
            toRelY = 1;
        }
        else {
            fromX = fromProcX + parseInt(fromProcW / 2);
            fromY = fromProcY;
            toX = toProcX + parseInt(toProcW / 2);
            toY = toProcY + toProcH;
            fromRelX = 0.5;
            fromRelY = 0;
            toRelX = 0.5;
            toRelY = 1;
        }
    }
    else if (fromProcX + fromProcW < toProcX) {
        fromX = fromProcX + fromProcW;
        fromY = fromProcY + parseInt(fromProcH / 2);
        toX = toProcX;
        toY = toProcY + parseInt(toProcH / 2);
        fromRelX = 1;
        fromRelY = 0.5;
        toRelX = 0;
        toRelY = 0.5;
    }
    else {
        fromX = fromProcX;
        fromY = fromProcY + parseInt(fromProcH / 2);
        toX = toProcX + toProcW;
        toY = toProcY + parseInt(toProcH / 2);
        fromRelX = 0;
        fromRelY = 0.5;
        toRelX = 1;
        toRelY = 0.5;
    }
    this.fromRelX = fromRelX;
    this.fromRelY = fromRelY;
    this.toRelX = toRelX;
    this.toRelY = toRelY;
    this.Points = fromX / 4 * 3 + 'pt,' + fromY / 4 * 3 + 'pt,' + toX / 4 * 3 + 'pt,' + toY / 4 * 3 + 'pt';
    return this.Points;
}
TStep.prototype.getPolyLinePath = function (fromProcX, fromProcY, fromProcW, fromProcH, toProcX, toProcY, toProcW, toProcH) {
    var fromCenterX = fromProcX + parseInt(fromProcW / 2);
    var fromCenterY = fromProcY + parseInt(fromProcH / 2);
    var toCenterX = toProcX + parseInt(toProcW / 2);
    var toCenterY = toProcY + parseInt(toProcH / 2);
    point2X = fromCenterX;
    point2Y = toCenterY;
    if (toCenterX > fromCenterX) {
        absY = toCenterY >= fromCenterY ? toCenterY - fromCenterY : fromCenterY - toCenterY;
        if (parseInt(fromProcH / 2) >= absY) {
            point1X = fromProcX + fromProcW;
            point1Y = toCenterY;
            point2X = point1X;
            point2Y = point1Y;
        }
        else {
            point1X = fromCenterX;
            point1Y = fromCenterY < toCenterY ? fromProcY + fromProcH : fromProcY;
        }
        absX = toCenterX - fromCenterX;
        if (parseInt(fromProcW / 2) >= absX) {
            point3X = fromCenterX;
            point3Y = fromCenterY < toCenterY ? toProcY : toProcY + toProcH;
            point2X = point3X;
            point2Y = point3Y;
        }
        else {
            point3X = toProcX;
            point3Y = toCenterY;
        }
    }
    if (toCenterX < fromCenterX) {
        absY = toCenterY >= fromCenterY ? toCenterY - fromCenterY : fromCenterY - toCenterY;
        if (parseInt(fromProcH / 2) >= absY) {
            point1X = fromProcX;
            point1Y = toCenterY;
            point2X = point1X;
            point2Y = point1Y;
        } else {
            point1X = fromCenterX;
            point1Y = fromCenterY < toCenterY ? fromProcY + fromProcH : fromProcY;
        }
        absX = fromCenterX - toCenterX;
        if (parseInt(fromProcW / 2) >= absX) {
            point3X = fromCenterX;
            point3Y = fromCenterY < toCenterY ? toProcY : toProcY + toProcH;
            point2X = point3X;
            point2Y = point3Y;
        }
        else {
            point3X = toProcX + toProcW;
            point3Y = toCenterY;
        }
    }
    if (toCenterX == fromCenterX) {
        point1X = fromCenterX;
        point1Y = fromCenterY > toCenterY ? fromProcY : fromProcY + fromProcH;
        point3X = fromCenterX;
        point3Y = fromCenterY > toCenterY ? toProcY + toProcH : toProcY;
        point2X = point3X;
        point2Y = point3Y;
    }
    if (toCenterY == fromCenterY) {
        point1X = fromCenterX > toCenterX ? fromProcX : fromProcX + fromProcW;
        point1Y = fromCenterY;
        point3Y = fromCenterY;
        point3X = fromCenterX > toCenterX ? toProcX + toProcW : toProcX;
        point2X = point3X;
        point2Y = point3Y;
    }
    return point1X + ',' + point1Y + ' ' + point2X + ',' + point2Y + ' ' + point3X + ',' + point3Y;
}
TStep.prototype.toStringWithoutLabel = function () {
    var StepHTML = '';
    var cl = this.Flow.Config;
    var arrVal = new Array();
    arrVal["id"] = this.ID;
    arrVal["title"] = this.ID + ':' + this.Text;
    arrVal["sc"] = cl.StepColor;
    arrVal["pt"] = this.getPath();
    arrVal["z"] = this.zIndex;
    arrVal["sw"] = this.StrokeWeight;
    arrVal["fsc"] = cl.StepFocusedStrokeColor;
    arrVal["sa"] = this.StartArrow;
    arrVal["ea"] = this.EndArrow;
    arrVal["cond"] = this.Cond;
    arrVal["text"] = this.Text;
    return stuffShape(getShapeVal('polylinenolabel'), arrVal);
}