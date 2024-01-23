package br.fiap.projeto.pagamento.bdd.acceptance;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class) //junit4 integrado ao 5, Cucumber n funciona direto cm o junit5 //Extends no junit5
@CucumberOptions(features = "classpath:features")
public class PagamentoCucumberRunner {

}
