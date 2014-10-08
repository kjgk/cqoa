Ext.define('withub.ext.system.user.UserOrganizationHistoryList', {
    extend: 'withub.ext.base.BaseGrid',
    height: 480,
    width: 600,
    title: '人员组织机构历史表',
    model: 'withub.ext.system.user.UserOrganizationHistoryModel',
    url: '/system/user/UserOrganizationHistory',
    autoQuery: true,
    enablePagginBar: false,
    initComponent: function () {

        this.extraParams = {
            userId: this.userId
        };
        this.columns = [
            Ext.create('Ext.grid.RowNumberer'),
            {text: '组织机构', flex: 1, dataIndex: 'organization'},
            {text: '进入日期', width: 100, dataIndex: 'enterDate', sortable: false, renderer: ExtUtil.dateRenderer(DateFormat.DAY)},
            {text: '离开日期', width: 100, dataIndex: 'leaveDate', sortable: false, renderer: ExtUtil.dateRenderer(DateFormat.DAY)}
        ];

        this.callParent();
    }
});

Ext.define('withub.ext.system.user.UserOrganizationHistoryModel', {
    extend: 'Ext.data.Model',
    fields: [
        'objectId',
        'user',
        'organization',
        'enterDate',
        'leaveDate'
    ]
});