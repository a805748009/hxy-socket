package nafos.core.entry;


import com.esotericsoftware.reflectasm.MethodAccess;

/**
* @author 作者 huangxinyu 
* @version 创建时间：2018年1月9日 下午5:17:02 
* 类说明 
*/
public class RouteClassAndMethod  extends  ClassAndMethod{


	protected boolean printLog;//是否打印方法时间

	protected String type; //JSON 或者 PRO[TOBUFF]

	protected boolean runOnWorkGroup;

	public RouteClassAndMethod(Class<?> clazz, MethodAccess method, Integer index, Class<?> paramType, boolean printLog, String type, boolean runOnWorkGroup) {
		super(clazz,method,index,paramType);
		this.printLog = printLog;
		this.type = type;
		this.runOnWorkGroup = runOnWorkGroup;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public MethodAccess getMethod() {
		return method;
	}

	public void setMethod(MethodAccess method) {
		this.method = method;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Class<?> getParamType() {
		return paramType;
	}

	public void setParamType(Class<?> paramType) {
		this.paramType = paramType;
	}

	public boolean isPrintLog() {
		return printLog;
	}

	public void setPrintLog(boolean printLog) {
		this.printLog = printLog;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isRunOnWorkGroup() {
		return runOnWorkGroup;
	}

	public void setRunOnWorkGroup(boolean runOnWorkGroup) {
		this.runOnWorkGroup = runOnWorkGroup;
	}
}
