package br.fiap.projeto.pagamento.bdd.acceptance;


import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;



@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
public class PagamentoCucumberRunner {

}
