package org.eod;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "QuandlEOD" Node.
 * 
 *
 * @author I Tang Lo
 */
public class QuandlEODNodeFactory 
        extends NodeFactory<QuandlEODNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public QuandlEODNodeModel createNodeModel() {
        return new QuandlEODNodeModel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNrNodeViews() {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeView<QuandlEODNodeModel> createNodeView(final int viewIndex,
            final QuandlEODNodeModel nodeModel) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDialog() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeDialogPane createNodeDialogPane() {
        return new QuandlEODNodeDialog();
    }

}

