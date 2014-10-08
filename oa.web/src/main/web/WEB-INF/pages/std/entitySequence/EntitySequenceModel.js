Ext.define('withub.ext.std.entitySequence.EntitySequenceModel', {
    extend: 'Ext.data.Model',
    fields: [
        'objectId',
        'name',
        'entity',
        'sequenceProperty',
        'fixedLength',
        'circleSequenceByYear',
        'yearProperty',
        'description'
    ]
});