/*
 *                  Eoulsan development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public License version 2.1 or
 * later and CeCILL-C. This should be distributed with the code.
 * If you do not have a copy, see:
 *
 *      http://www.gnu.org/licenses/lgpl-2.1.txt
 *      http://www.cecill.info/licences/Licence_CeCILL-C_V1-en.txt
 *
 * Copyright for this code is held jointly by the Genomic platform
 * of the Institut de Biologie de l'École normale supérieure and
 * the individual authors. These should be listed in @author doc
 * comments.
 *
 * For more information on the Eoulsan project and its aims,
 * or to join the Eoulsan Google group, visit the home page
 * at:
 *
 *      http://outils.genomique.biologie.ens.fr/eoulsan
 *
 */

package fr.ens.biologie.genomique.kenetre.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * This class contains some useful methods about collection, hashcode
 * computation or precondition checking.
 * @since 1.0
 * @author Laurent Jourdren
 */
public class Utils {

  /**
   * Reverse a map
   * @param map Map to reverse
   * @return The reverse map
   */
  public static <K, V> Map<V, Set<K>> reverseMap(final Map<K, V> map) {

    if (map == null) {
      return null;
    }

    final Map<V, Set<K>> result = new HashMap<>();

    for (Map.Entry<K, V> e : map.entrySet()) {

      final Set<K> set;

      final V value = e.getValue();

      if (!result.containsKey(value)) {
        set = new HashSet<>();
        result.put(value, set);
      } else {
        set = result.get(value);
      }

      set.add(e.getKey());
    }

    return result;
  }

  /**
   * Create an unmodifiableSet from an array
   * @param array array with the values of the output Set
   * @return an unmodifiableSet with the values of the array or null if the
   *         array is null
   */
  public static <E> Set<E> unmodifiableSet(final E[] array) {

    if (array == null) {
      return null;
    }

    final List<E> list = Arrays.asList(array);

    return Collections.unmodifiableSet(new HashSet<>(list));
  }

  /**
   * Return a list without null elements.
   * @param list input list
   * @return a list without null elements
   */
  public static <E> List<E> listWithoutNull(final List<E> list) {

    if (list == null) {
      return null;
    }

    final List<E> result = new ArrayList<>();

    for (E e : list) {
      if (e != null) {
        result.add(e);
      }
    }

    return result;
  }

  /**
   * Determines whether two possibly-null objects are equal.
   * @param a the first object
   * @param b the second object
   * @return true if the objects are equals
   */
  public static boolean equal(final Object a, final Object b) {

    return Objects.equals(a, b);
  }

  /**
   * Generates a hash code for multiple values. The hash code is generated by
   * calling {@link Arrays#hashCode(Object[])}.
   * @param objects a list of objects
   */
  public static int hashCode(final Object... objects) {
    return Arrays.hashCode(objects);
  }

  /**
   * Throw a NullPointerException if the input object is null.
   * @param object object to test
   * @param msg error message
   */
  public static void checkNotNull(final Object object, final String msg) {

    if (object == null) {
      throw new NullPointerException(msg);
    }
  }

  /**
   * Throws a IllegalStateException if the expression if false.
   * @param expression expression to test
   * @param msg error message
   */
  public static void checkState(final boolean expression, final String msg) {

    if (!expression) {
      throw new IllegalStateException(msg);
    }
  }

  /**
   * Create an ArrayList Object.
   * @param elements to add at the creation of the list
   * @return a new ArrayList object
   */
  public static <E> List<E> newArrayList(
      final Collection<? extends E> elements) {

    return new ArrayList<>(elements);
  }

  /**
   * Create a HashSet Object.
   * @param elements to add at the creation of the set
   * @return a new HashSet object
   */
  public static <E> HashSet<E> newHashSet(
      final Collection<? extends E> elements) {

    return new HashSet<>(elements);
  }

  /**
   * Do nothing.
   */
  public static void nop() {
  }

  /**
   * This method wrap an Enumeration object into an Iterable object.
   * @param e Enumeration to wrap
   * @return an Iterable object
   */
  public static <T> Iterable<T> newIterable(final Enumeration<T> e) {

    if (e == null) {
      return null;
    }

    return () -> new Iterator<T>() {

      @Override
      public boolean hasNext() {
        return e.hasMoreElements();
      }

      @Override
      public T next() {
        return e.nextElement();
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

  /**
   * This method wrap an Iterator object into an Iterable object.
   * @param it Iterator to wrap
   * @return an Iterable object
   */
  public static <T> Iterable<T> newIterable(final Iterator<T> it) {

    if (it == null) {
      return null;
    }

    return () -> it;
  }

  /**
   * Filter null value from an array.
   * @param array array to filter
   * @return an array without null elements
   */
  @SuppressWarnings("unchecked")
  public static <E> E[] filterNull(E[] array) {

    if (array == null) {
      return null;
    }

    List<E> list = new ArrayList<>(array.length);

    for (E e : array) {
      if (e != null) {
        list.add(e);
      }
    }

    return (E[]) list.toArray(new Object[0]);
  }

  /**
   * Filter null value from a list.
   * @param list list to filter
   * @return a list without null elements
   */
  public static <E> List<E> filterNull(List<E> list) {

    if (list == null) {
      return null;
    }

    List<E> result = new ArrayList<>(list.size());

    for (E e : list) {
      if (e != null) {
        result.add(e);
      }
    }

    return result;
  }

  /**
   * Filter null value from a set.
   * @param set set to filter
   * @return a set without null element
   */
  public static <E> Set<E> filterNull(Set<E> set) {

    if (set == null) {
      return null;
    }

    Set<E> result = new HashSet<>(set.size());
    result.remove(null);

    return result;
  }

  /**
   * Remove null values from a collection.
   * @param collection collection to filter
   */
  public static <E> void removeAllNull(Collection<E> collection) {

    if (collection == null) {
      return;
    }

    List<E> toRemove = new ArrayList<>();

    for (E e : collection) {
      if (e == null) {
        toRemove.add(e);
      }
    }

    collection.removeAll(toRemove);
  }

}