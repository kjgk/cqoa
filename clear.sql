DELETE FROM WF_INSTANCEORGANIZATION;
DELETE FROM WF_INSTANCE;
DELETE FROM WF_SUBINSTANCE;
DELETE FROM WF_MASTERTASK;
DELETE FROM WF_TASK;
DELETE FROM WF_TASKCONTEXT;
DELETE FROM WF_TASKOPINION;
DELETE FROM WF_ENTITYMODIFYTASK;

DELETE FROM OA_CARUSEINFO;
DELETE FROM OA_CARUSEUSER;
DELETE FROM OA_CARUSE;
DELETE FROM OA_LEAVE;
DELETE FROM OA_MEETINGROOM;
DELETE FROM OA_MISCELLANEOUS;
DELETE FROM OA_OUTGOINGUSER;
DELETE FROM OA_OUTGOING;
DELETE FROM OA_TRAINING;


DELETE FROM STD_AGENCY;
DELETE FROM STD_APPVERSION;
DELETE FROM STD_BULLETIN;
DELETE FROM STD_CALENDARPLAN;
DELETE FROM STD_COMMONTEXT;
DELETE FROM STD_EVENTNOTIFYSERVICETYPE;
DELETE FROM STD_FILEINFO;
DELETE FROM STD_NOTIFYQUEUEHISTORY;
DELETE FROM STD_SENDQUEUEHISTORY;
DELETE FROM SYS_USERORGANIZATIONHISTORY;
DELETE FROM SYS_AUTHORIZATIONCLUSTERCACHE;
DELETE FROM SYS_PASSWORDHISTORY;

-- DELETE FROM SYS_ORGANIZATION WHERE PARENTID IS NOT NULL ;
-- DELETE FROM SYS_ACCOUNT WHERE USERID != '95F896C2-B6FF-4E1C-95A4-9D5DD5E71FF8' ;
-- DELETE FROM SYS_USER WHERE OBJECTID NOT IN ('95F896C2-B6FF-4E1C-95A4-9D5DD5E71FF8' , 'EE033C3D-6CA1-4148-921E-4CE04C9BBD9A')

UPDATE SYS_MENU SET CREATOR = 'EE033C3D-6CA1-4148-921E-4CE04C9BBD9A', LASTEDITOR  = 'EE033C3D-6CA1-4148-921E-4CE04C9BBD9A';
UPDATE SYS_PERMISSION SET CREATOR = 'EE033C3D-6CA1-4148-921E-4CE04C9BBD9A', LASTEDITOR  ='EE033C3D-6CA1-4148-921E-4CE04C9BBD9A';
UPDATE SYS_ENTITY SET CREATOR = 'EE033C3D-6CA1-4148-921E-4CE04C9BBD9A', LASTEDITOR  ='EE033C3D-6CA1-4148-921E-4CE04C9BBD9A';
UPDATE STD_FILECONFIG SET CREATOR = 'EE033C3D-6CA1-4148-921E-4CE04C9BBD9A', LASTEDITOR  ='EE033C3D-6CA1-4148-921E-4CE04C9BBD9A';
UPDATE WF_RAMUS SET CREATOR = 'EE033C3D-6CA1-4148-921E-4CE04C9BBD9A', LASTEDITOR  ='EE033C3D-6CA1-4148-921E-4CE04C9BBD9A';
UPDATE WF_RAMUSREGULATION SET CREATOR = 'EE033C3D-6CA1-4148-921E-4CE04C9BBD9A', LASTEDITOR  ='EE033C3D-6CA1-4148-921E-4CE04C9BBD9A';
UPDATE WF_FLOWTYPE SET CREATOR = 'EE033C3D-6CA1-4148-921E-4CE04C9BBD9A', LASTEDITOR  = 'EE033C3D-6CA1-4148-921E-4CE04C9BBD9A';
UPDATE WF_FLOWNODE SET CREATOR = 'EE033C3D-6CA1-4148-921E-4CE04C9BBD9A', LASTEDITOR  = 'EE033C3D-6CA1-4148-921E-4CE04C9BBD9A';





