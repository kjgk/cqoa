package com.withub.service.system;

import com.withub.model.system.po.Code;
import com.withub.model.system.po.CodeColumn;
import com.withub.model.system.po.CodeColumnCategory;


public interface CodeService extends com.withub.service.EntityService {

    public CodeColumnCategory getRootCodeColumnCategory() throws Exception;

    public CodeColumn getCodeColumnByTag(String codeColumnTag) throws Exception;

    public CodeColumn getCodeColumnById(String codeColumnId) throws Exception;

    public Code getCodeById(String codeId) throws Exception;

    public Code getCodeByTag(String codeColumnTag, String codeTag) throws Exception;

    public Code getCodeByEnum(Enum enumObject) throws Exception;

    public String parseDisplayText(Code code) throws Exception;

    public Code getDefaultCode(String codeColumnTag) throws Exception;
}