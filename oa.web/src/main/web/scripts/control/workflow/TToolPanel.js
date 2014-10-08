function TToolPanel(id, l, t, w, h, content, mw)
{
    this.ID = id;
    this.Left = l;
    this.Top = t;
    this.Width = w;
    this.MinWidth = mw ? mw : w;
    this.Height = h;
    this.Content = content;
    this.InnerObject = null;
    document.write(this);
    this.InnerObject = document.all(this.ID);
    _ToolBoxManager[_ToolBoxManager.length] = this;
}
;
TToolPanel.prototype.setZIndex = function(i)
{
    this.InnerObject.style.zIndex = i;
};
TToolPanel.prototype.toString = function()
{
    S = '<table id="' + this.ID + '" class="toolbox" style="position:absolute;left:' + this.Left + 'px;top:' + this.Top + 'px;width:' +
        this.Width + 'px;height:' + this.Height + 'px;z-index:3000" cellspacing="0" cellpadding="0" border="0" onmousedown="' + this.ID + '.mouseDown();" ' + 'onmousemove="' + this.ID + '.mouseMove();" onmouseup="' + this.ID + '.mouseUp();">' + '<tr><td width="8" style="cursor:move;" moveType="m"><img moveType="m" src="image/tb_title.gif" height="25" width="5" border="0"></td><td>' + this.Content + '</td</tr>' + '</table>';
    return S;
};
TToolPanel.prototype.mouseDown = function()
{
    if (event.button != 1)return;
    var obj = event.srcElement;
    if (!obj.moveType)return;
    this.moveType = obj.moveType;
    this.InnerObject.setCapture();
    this.CurrentX = event.x - this.InnerObject.offsetLeft;
    this.CurrentY = event.y - this.InnerObject.offsetTop;
    setToolBoxTopMost(this);
};
TToolPanel.prototype.mouseMove = function()
{
    switch (this.moveType)
    {case"m":this.Left = event.x - this.CurrentX;this.Top = event.y - this.CurrentY
        this.InnerObject.style.left = this.Left;this.InnerObject.style.top = this.Top;break;
    }
};
TToolPanel.prototype.mouseUp = function()
{
    this.moveType = "";
    this.InnerObject.releaseCapture();
    if (parseInt(this.InnerObject.style.top) < -10)
    {
        alert("工具栏上边界超出边界，将自动调整.");
        this.InnerObject.style.top = 0;
    }
};