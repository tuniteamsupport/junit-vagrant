package org.kafkaliu.test.vagrant.ruby;

import org.jruby.RubyArray;
import org.jruby.RubyObject;
import org.jruby.RubySymbol;
public class VagrantMachine {

	private VagrantEnvironment vagrantEnv;

	public VagrantMachine(VagrantEnvironment vagrantEnv) {
		super();
		this.vagrantEnv = vagrantEnv;
	}
	
	public RubyObject[] getMachines() {
		RubyArray machineNames = (RubyArray) vagrantEnv.callCli("machine_names");
		RubyObject[] machines = new RubyObject[machineNames.size()];
		for (int i = 0; i < machineNames.size(); i++) {
			RubySymbol machineName = (RubySymbol) machineNames.get(i);
			machines[i] = getMachine(machineName.asJavaString());
		}
		return machines;
	}
	
	public RubyObject getMachine(String vmName) {
		return method("machine", vmName, "virtualbox");
	}
	
	private RubyObject method(String name, String... args) {
		return (RubyObject) vagrantEnv.callMethod(name, args);
	}

}
