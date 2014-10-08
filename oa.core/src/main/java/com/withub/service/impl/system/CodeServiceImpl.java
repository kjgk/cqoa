package com.withub.service.impl.system;

import com.withub.common.util.StringUtil;
import com.withub.model.system.po.Code;
import com.withub.model.system.po.CodeColumn;
import com.withub.model.system.po.CodeColumnCategory;
import com.withub.service.EntityServiceImpl;
import com.withub.service.system.CodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("codeService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class CodeServiceImpl extends EntityServiceImpl implements CodeService {

    //=================== 接口实现 =========================================================


    public CodeColumnCategory getRootCodeColumnCategory() throws Exception {

        CodeColumnCategory category = (CodeColumnCategory) getByHql("from " + CodeColumnCategory.class.getName() + "" +
                " where parent is null and objectStatus = 1 order by orderNo");
        return category;
    }

    public CodeColumn getCodeColumnByTag(String codeColumnTag) throws Exception {

        CodeColumn codeColumn = (CodeColumn) getByPropertyValue(CodeColumn.class, "codeColumnTag", codeColumnTag);
        return codeColumn;
    }

    public CodeColumn getCodeColumnById(String codeColumnId) throws Exception {

        CodeColumn codeColumn = (CodeColumn) get(CodeColumn.class, codeColumnId);
        return codeColumn;
    }

    public Code getCodeByTag(String codeColumnTag, String codeTag) throws Exception {

        codeTag = codeTag.trim().toLowerCase();
        CodeColumn codeColumn = (CodeColumn) getByPropertyValue(CodeColumn.class, "codeColumnTag", codeColumnTag);

        for (Code code : codeColumn.getCodeList()) {
            if (StringUtil.isNotEmpty(code.getCodeTag()) && code.getCodeTag().trim().equalsIgnoreCase(codeTag)) {
                return code;
            }
        }

        return null;
    }

    public Code getCodeByEnum(Enum enumObject) throws Exception {

        String codeColumnTag = enumObject.getClass().getSimpleName();
        String codeTag = enumObject.toString();
        Code code = getCodeByTag(codeColumnTag, codeTag);

        return code;
    }

    public Code getCodeById(String codeId) throws Exception {

        return (Code) get(Code.class, codeId);
    }

    public Code getDefaultCode(String codeColumnTag) throws Exception {

        Code defaultCode = null;
        CodeColumn codeColumn = getCodeColumnByTag(codeColumnTag);
        for (Code code : codeColumn.getCodeList()) {
            if (code.getDefaultValue() == 1) {
                return code;
            }
        }
        return defaultCode;
    }

    public String parseDisplayText(Code code) throws Exception {

        String displayRegulation = code.getCodeColumn().getDisplayRegulation();

        if (StringUtil.isEmpty(displayRegulation)) {
            return code.getName();
        }

        String codeTag = code.getCodeTag() == null ? "" : code.getCodeTag();
        String descripiton = code.getDescription() == null ? "" : code.getDescription();

        String text = displayRegulation.replace("{name}", code.getName())
                .replace("{codeTag}", codeTag)
                .replace("{descripiton}", descripiton);

        return text;
    }
}