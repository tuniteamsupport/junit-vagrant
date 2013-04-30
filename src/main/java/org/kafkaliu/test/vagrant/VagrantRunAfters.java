package org.kafkaliu.test.vagrant;

import java.util.ArrayList;
import java.util.List;

import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;
import org.kafkaliu.test.vagrant.annotations.VagrantConfigure;
import org.kafkaliu.test.vagrant.ruby.VagrantCli;
import org.kafkaliu.test.vagrant.ruby.VagrantEnvironment;

public class VagrantRunAfters extends Statement {
	
	private Statement statement;
	
	private VagrantCli cli;
	
	private Class<?> klass;
	
	public VagrantRunAfters(Statement statement, VagrantEnvironment vagrantEnv, Class<?> klass) {
		super();
		this.statement = statement;
		this.cli = new VagrantCli(vagrantEnv);
		this.klass = klass;
	}

	@Override
	public void evaluate() throws Throwable {
        List<Throwable> errors = new ArrayList<Throwable>();
        try {
        	statement.evaluate();
        } catch (Throwable e) {
            errors.add(e);
        } finally {
        	VagrantConfigure annotation = klass.getAnnotation(VagrantConfigure.class);
        	if (annotation != null && annotation.needDestroyVmAfterClassTest()) {
        		cli.destroy();
        	} else {
	        	cli.ssh("killall java");
	        	Thread.sleep(5 * 1000);
        	}
        }
        MultipleFailureException.assertEmpty(errors);
	}

}