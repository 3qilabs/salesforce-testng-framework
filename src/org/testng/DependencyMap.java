
package org.testng;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.testng.collections.ListMultiMap;
import org.testng.collections.Lists;
import org.testng.collections.Maps;


/**
 * Helper class to keep track of dependencies.
 *
 */
public class DependencyMap {
  private ListMultiMap<String, ITestNGMethod> m_dependencies = Maps.newListMultiMap();
  private ListMultiMap<String, ITestNGMethod> m_groups = Maps.newListMultiMap();

  public DependencyMap(ITestNGMethod[] methods) {
    for (ITestNGMethod m : methods) {
    	m_dependencies.put( m.getQualifiedName(), m);
      for (String g : m.getGroups()) {
        m_groups.put(g, m);
      }
    }
  }

  public List<ITestNGMethod> getMethodsThatBelongTo(String group, ITestNGMethod fromMethod) {
    Set<String> uniqueKeys = m_groups.keySet();

    List<ITestNGMethod> result = Lists.newArrayList();

    for (String k : uniqueKeys) {
      if (Pattern.matches(group, k)) {
        result.addAll(m_groups.get(k));
      }
    }

    if (result.isEmpty() && !fromMethod.ignoreMissingDependencies()) {
      throw new TestNGException("DependencyMap::Method \"" + fromMethod
          + "\" depends on nonexistent group \"" + group + "\"");
    } else {
      return result;
    }
  }

  public ITestNGMethod getMethodDependingOn(String methodName, ITestNGMethod fromMethod) {
    List<ITestNGMethod> l = m_dependencies.get(methodName);
    if (l.isEmpty() && fromMethod.ignoreMissingDependencies()){
    	return fromMethod;
    }
    for (ITestNGMethod m : l) {
      // If they are in the same class hierarchy, they must belong to the same instance,
      // otherwise, it's a method depending on a method in a different class so we
      // don't bother checking the instance
      if (fromMethod.getRealClass().isAssignableFrom(m.getRealClass())) {
        if (m.getInstance() == fromMethod.getInstance())
//        		|| m.getRealClass().isAssignableFrom(Scenario.class))
        	return m;
      } else {
        return m;
      }
    }
    throw new TestNGException("Method \"" + fromMethod
        + "\" depends on nonexistent method \"" + methodName + "\"");
  }
}
