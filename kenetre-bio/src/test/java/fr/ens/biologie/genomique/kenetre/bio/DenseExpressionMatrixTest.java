package fr.ens.biologie.genomique.kenetre.bio;

public class DenseExpressionMatrixTest extends AbstractExpressionMatrixTest {

  @Override
  protected ExpressionMatrix createMatrix() {

    return new DenseExpressionMatrix();
  }

  @Override
  protected ExpressionMatrix createMatrix(double defaultValue) {

    return new DenseExpressionMatrix(defaultValue);
  }

}
