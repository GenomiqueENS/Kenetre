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

package fr.ens.biologie.genomique.kenetre.bio.alignmentfilter;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fr.ens.biologie.genomique.kenetre.util.ReporterIncrementer;
import fr.ens.biologie.genomique.kenetre.KenetreException;
import fr.ens.biologie.genomique.kenetre.log.DummyLogger;
import fr.ens.biologie.genomique.kenetre.log.GenericLogger;

/**
 * This builder allow to create a MultiAlignmentsFilter object.
 * @since 1.1
 * @author Laurent Jourdren
 */
public class MultiReadAlignmentFilterBuilder {

  private GenericLogger logger = new DummyLogger();
  private final Map<String, ReadAlignmentFilter> mapFilters = new HashMap<>();
  private final List<ReadAlignmentFilter> listFilter = new ArrayList<>();
  private final Map<String, String> mapParameters = new LinkedHashMap<>();

  /**
   * Set the logger to use.
   * @param logger the logger to use
   */
  private void setLogger(GenericLogger logger) {

    requireNonNull(logger);
    this.logger = logger;
  }

  /**
   * Get the logger.
   * @return the logger
   */
  public GenericLogger getLogger() {

    return this.logger;
  }

  /**
   * Add a parameter to the builder
   * @param key key of the parameter
   * @param value value of the parameter
   * @return true if the parameter has been successfully added
   * @throws KenetreException if the filter reference in the key does not exist
   *           or if an error occurs while setting the parameter in the
   *           dedicated filter
   */
  public boolean addParameter(final String key, final String value)
      throws KenetreException {

    return addParameter(key, value, false);
  }

  /**
   * Add a parameter to the builder
   * @param key key of the parameter
   * @param value value of the parameter
   * @param noExceptionIfFilterNotExists do not thrown an exception if the
   *          filter does not exists.
   * @return true if the parameter has been successfully added
   * @throws KenetreException if the filter reference in the key does not exist
   *           or if an error occurs while setting the parameter in the
   *           dedicated filter
   */
  public boolean addParameter(final String key, final String value,
      final boolean noExceptionIfFilterNotExists) throws KenetreException {

    if (key == null || value == null) {
      return false;
    }

    // Get first dot position
    final String keyTrimmed = key.trim();
    final int index = keyTrimmed.indexOf('.');

    final String filterName;
    final String filterKey;

    // Get the filter name and parameter name
    if (index == -1) {
      filterName = keyTrimmed;
      filterKey = null;
    } else {
      filterName = keyTrimmed.substring(0, index);
      filterKey = keyTrimmed.substring(index + 1);
    }

    final ReadAlignmentFilter filter;

    // Get the filter object, load it if necessary
    if (this.mapFilters.containsKey(filterName)) {
      filter = this.mapFilters.get(filterName);
    } else {
      filter = ReadAlignmentFilterService.getInstance().newService(filterName);

      if (filter == null) {

        if (noExceptionIfFilterNotExists) {
          return false;
        }

        throw new KenetreException(
            "Unable to find " + filterName + " alignments filter.");
      }

      filter.setLogger(this.logger);
      this.mapFilters.put(filterName, filter);
      this.listFilter.add(filter);
    }

    // Set the parameter
    if (filterKey != null) {
      final String valueTrimmed = value.trim();
      filter.setParameter(filterKey, valueTrimmed);
      this.mapParameters.put(keyTrimmed, valueTrimmed);
      getLogger().info("Set alignments filter \""
          + filterName + "\" with parameter: " + filterKey + "="
          + valueTrimmed);
    } else {
      this.mapParameters.put(filterName, "");
      getLogger().info(
          "Set alignments filter \"" + filterName + "\" with no parameter");
    }

    return true;
  }

  /**
   * Add parameters to the builder.
   * @param parameters parameters to add
   * @throws KenetreException if the filter reference in the key does not exist
   *           or if an error occurs while setting the parameter in the
   *           dedicated filter
   */
  public void addParameters(final Map<String, String> parameters)
      throws KenetreException {

    if (parameters == null) {
      return;
    }

    for (Map.Entry<String, String> e : parameters.entrySet()) {
      addParameter(e.getKey(), e.getValue());
    }
  }

  /**
   * Create the final MultiAlignmentFilter.
   * @return a new MultiAlignmentsFilter object
   * @throws KenetreException if an error occurs while initialize one of the
   *           filter
   */
  public MultiReadAlignmentFilter getAlignmentFilter() throws KenetreException {

    for (ReadAlignmentFilter f : this.listFilter) {
      f.init();
    }

    return new MultiReadAlignmentFilter(this.listFilter);
  }

  /**
   * Create the final MultiAlignmentsFilter.
   * @param incrementer incrementer to use
   * @param counterGroup counter group for the incrementer
   * @return a new MultiAlignmentsFilter object
   * @throws KenetreException if an error occurs while initialize one of the
   *           filter
   */
  public MultiReadAlignmentFilter getAlignmentFilter(
      final ReporterIncrementer incrementer, final String counterGroup)
      throws KenetreException {

    for (ReadAlignmentFilter f : this.listFilter) {
      f.init();
    }

    return new MultiReadAlignmentFilter(incrementer, counterGroup,
        this.listFilter);
  }

  /**
   * Get a map with all the parameters used to create the MultiAlignmentsFilter.
   * @return an ordered map object
   */
  public Map<String, String> getParameters() {

    return Collections.unmodifiableMap(this.mapParameters);
  }

  //
  // Constructors
  //

  /**
   * Public constructor.
   */
  public MultiReadAlignmentFilterBuilder() {
  }

  /**
   * Public constructor.
   * @param logger the logger to use
   */
  public MultiReadAlignmentFilterBuilder(final GenericLogger logger) {

    setLogger(logger);
  }

  /**
   * Public constructor.
   * @param parameters parameters to add to the builder
   * @throws KenetreException if the filter reference in the key does not exist
   *           or if an error occurs while setting the parameter in the
   *           dedicated filter
   */
  public MultiReadAlignmentFilterBuilder(final Map<String, String> parameters)
      throws KenetreException {

    addParameters(parameters);
  }

  /**
   * Public constructor.
   * @param logger the logger to use
   * @param parameters parameters to add to the builder
   * @throws KenetreException if the filter reference in the key does not exist
   *           or if an error occurs while setting the parameter in the
   *           dedicated filter
   */
  public MultiReadAlignmentFilterBuilder(final GenericLogger logger,
      final Map<String, String> parameters) throws KenetreException {

    setLogger(logger);
    addParameters(parameters);
  }

}
