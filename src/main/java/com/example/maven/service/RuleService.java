package com.example.maven.service;

import com.example.maven.domain.DroolsFile;
import com.example.maven.domain.Rule;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@Singleton
public class RuleService {
    @Inject
    private FreeMarketTemplateService freeMarketTemplateService;

    public Map<String,String> loadRuleFromDBIntoDrlFile() throws SQLException {
        Connection connection = DBConnection.getConnection("root", "India@2022");
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(getQuery());
            DroolsFile droolsFile = buildDroolsFileDataFromDB(resultSet);
            Map<String,String> map=new HashMap<>();
            map.put("drlFilePath",generateDrlFile(droolsFile));
            map.put("numOfRule",String.valueOf(droolsFile.getRules().size()));
            return map;
        } finally {
            assert statement != null;
            statement.close();
            assert resultSet != null;
            resultSet.close();
        }

    }

    private DroolsFile buildDroolsFileDataFromDB(ResultSet resultSet) throws SQLException {
        DroolsFile droolsFile = new DroolsFile();
        List<Rule> rules = new ArrayList<>();
        while (resultSet.next()) {
            droolsFile.setId(resultSet.getLong("droolsfileId"));
            droolsFile.setGlobalContent(resultSet.getString("droolsfileGlobalContent"));
            droolsFile.setCreatedDate(resultSet.getDate("droolsfileCreatedDate"));
            droolsFile.setUpdatedDate(resultSet.getDate("droolsfileUpdatedDate"));

            Rule rule = new Rule();
            rule.setRuleAction(resultSet.getString("ruleAction"));
            rule.setRuleName(resultSet.getString("ruleName"));
            rule.setRuleCondition(resultSet.getString("ruleCondition"));
            rules.add(rule);
        }
        droolsFile.setRules(rules);
        return droolsFile;
    }

    private String generateDrlFile(DroolsFile droolsFile) {
        return freeMarketTemplateService.generateDrl(droolsFile);
    }

    private String getQuery() {
        return "select\n" +
                "        droolsfile.id as droolsfileId,\n" +
                "        droolsfile.created_date as droolsfileCreatedDate,\n" +
                "        droolsfile.global_content as droolsfileGlobalContent,\n" +
                "        droolsfile.updated_date as droolsfileUpdatedDate,\n" +
                "        rule.id as ruleId,\n" +
                "        rule.created_date as ruleCreatedDate,\n" +
                "        rule.rule_action as ruleAction,\n" +
                "        rule.rule_condition as ruleCondition,\n" +
                "        rule.rule_name as ruleName,\n" +
                "        rule.updated_date as ruleUpdatedDate \n" +
                "    from\n" +
                "        drools_poc.drools_file droolsfile \n" +
                "    left outer join\n" +
                "        drools_poc.drools_file_rules mapping \n" +
                "            on droolsfile.id=mapping.drools_file_id \n" +
                "    left outer join\n" +
                "        drools_poc.drools_rule rule \n" +
                "            on mapping.rules_id=rule.id \n" +
                "    where\n" +
                "        droolsfile.id=1";
    }
}
