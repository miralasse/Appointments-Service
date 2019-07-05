import React from 'react';

class Button extends React.Component {

    render() {

        return (
            <button className="btn btn-primary"
                    onClick={this.props.handleShowForm}>
                Добавить расписание
            </button>
        );
    }

}

export default Button;
