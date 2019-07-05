import React from 'react';


class Pagination extends React.Component {

    isCurrentPage = (value) => {
        return 'page-item ' + ((value === this.props.pageNumber) ? 'active' : '');
    };

    render() {

        const pageNumbers = [...Array(this.props.totalPages).keys()];

        const numbersToDisplay = pageNumbers.map((pageNumber) =>
            <li className={this.isCurrentPage(pageNumber)} key={pageNumber.toString()}>
                <button className="page-link" onClick={() => this.props.changePageNumber(pageNumber)}>{pageNumber + 1}</button>
            </li>
        );

        return (
            <nav aria-label="schedulesTablePagination">
                <ul className="pagination justify-content-center">
                    {numbersToDisplay}
                </ul>
            </nav>
        );
    }

}

export default Pagination;
