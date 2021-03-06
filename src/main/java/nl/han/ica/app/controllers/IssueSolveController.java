/**
 * Copyright 2013 
 * 
 * HAN University of Applied Sciences
 * Maik Diepenbroek
 * Wouter Konecny
 * Sjoerd van den Top
 * Teun van Vegchel
 * Niek Versteege
 *
 * See the file MIT-license.txt for copying permission.
 */


package nl.han.ica.app.controllers;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import nl.han.ica.app.models.IssueSolvingService;
import nl.han.ica.app.models.parameter.ParameterChangeListener;
import nl.han.ica.app.models.parameter.ParameterEvent;
import nl.han.ica.core.Delta;
import nl.han.ica.core.Job;
import nl.han.ica.core.Solution;
import nl.han.ica.core.issue.Issue;

import java.io.IOException;

/**
 * Handles the solving of a single issue. Displays the issue's details and solution, and provides the controls to apply
 * or ignore that solution.
 */
public class IssueSolveController extends BaseController {

    private Job job;
    private Issue issue;
    private Solution solution;
    private IssueSolvingService issueSolvingService;

    @FXML
    protected Label issueTitle;
    @FXML
    protected Label issueDescription;
    @FXML
    protected VBox deltasContainer;

    /**
     * Creates a Issue Resolve Controller This controller is reused by multiple issues. By setting a new issue using
     * the {@link this.setIssue(Issue)} method, the view is updated with the new issue's data.
     *
     * @param job The job for the controller.
     */
    public IssueSolveController(Job job) {
        this.job = job;
        issueSolvingService = new IssueSolvingService(job);
        issueSolvingService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                showDeltas();
            }
        });
    }

    /**
     * Update the view with the current issue's information.
     */
    private void handleIssueChanged() {
        logger.info("Updating issue view");

        issueTitle.setText(issue.getDetector().getTitle());
        issueDescription.setText(issue.getDetector().getDescription());

        issueSolvingService.setIssue(issue);
        issueSolvingService.setParameters(null);
        issueSolvingService.restart();
    }

    /**
     * Show the list of deltas belonging to the current issue.
     */
    private void showDeltas() {
        deltasContainer.getChildren().clear();

        solution = issueSolvingService.getValue();
        for (Delta delta : solution.getDeltas()) {
            IssueSolveDeltaController deltaController = new IssueSolveDeltaController(delta, solution.getParameters());
            deltaController.addParameterChangeListener(new ParameterChangeListener() {
                @Override
                public void changed(ParameterEvent event) {
                    issueSolvingService.restart();
                }
            });
            deltasContainer.getChildren().add(deltaController.getView());
        }
    }

    /**
     * Executed when the Apply button is clicked in the interface.
     */
    @FXML
    protected void applySolution() {
        job.applySolution(solution);
    }

    /**
     * Ignores a solution. Removes it from the issues list.
     */
    @FXML
    protected void ignoreSolution() {
        job.ignoreSolution(issue);
    }

    @Override
    public Pane getView() {
        try {
            return (Pane) buildView("/views/issue_solve.fxml");
        } catch (IOException e) {
            logger.fatal("Could not build the view from the FXML document.", e);
            return null;
        }
    }

    /**
     * Gets the current issue.
     *
     * @return The current issue.
     */
    public Issue getIssue() {
        return issue;
    }

    /**
     * Sets the current issue.
     *
     * @param issue The issue to set.
     */
    public void setIssue(Issue issue) {
        if (this.issue != issue) {
            this.issue = issue;
            handleIssueChanged();
        }
    }

}
