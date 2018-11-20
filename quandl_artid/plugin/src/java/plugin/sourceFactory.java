package plugin;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "source" Node.
 *
 */
public class sourceFactory
        extends NodeFactory<sourceModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public sourceModel createNodeModel() {
        return new sourceModel();
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
    public NodeView<sourceModel> createNodeView(final int viewIndex,
            final sourceModel nodeModel) {
        return new sourceView(nodeModel);
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
        return new sourceDialog();
    }

}
