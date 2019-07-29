package nafos.core.entry;

import nafos.core.entry.error.BizException;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年4月9日 下午5:49:12
 * 类说明
 */
public class ResultStatus {
    private boolean success;

    private BizException bizException;

    public ResultStatus() {
    }

    public ResultStatus(boolean success) {
        this.success = success;
    }

    public ResultStatus(boolean success, BizException bizException) {
        this.success = success;
        this.bizException = bizException;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public BizException getBizException() {
        return bizException;
    }

    public void setBizException(BizException bizException) {
        this.bizException = bizException;
    }

    @Override
    public String toString() {
        return "ResultStatus{" +
                "success=" + success +
                ", bizException=" + bizException +
                '}';
    }
}
