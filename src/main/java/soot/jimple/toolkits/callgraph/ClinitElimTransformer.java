package soot.jimple.toolkits.callgraph;

/*-
 * #%L
 * Soot - a J*va Optimization Framework
 * %%
 * Copyright (C) 2003 Jennifer Lhotak
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */

import java.util.Iterator;
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.Scene;
import soot.SootMethod;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.scalar.FlowSet;

public class ClinitElimTransformer extends BodyTransformer {

  protected void internalTransform(Body b, String phaseName, Map options) {
    ClinitElimAnalysis a = new ClinitElimAnalysis(new BriefUnitGraph(b));

    CallGraph cg = Scene.v().getCallGraph();

    SootMethod m = b.getMethod();

    Iterator edgeIt = cg.edgesOutOf(m);

    while (edgeIt.hasNext()) {
      Edge e = (Edge) edgeIt.next();
      if (e.srcStmt() == null) {
        continue;
      }
      if (!e.isClinit()) {
        continue;
      }
      FlowSet methods = (FlowSet) a.getFlowBefore(e.srcStmt());
      if (methods.contains(e.tgt())) {
        cg.removeEdge(e);
      }
    }
  }
}
