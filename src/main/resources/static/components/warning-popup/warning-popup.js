import React from 'react';
import ReactDOM from 'react-dom';

import './warning-popup.css';


export class WarningPopup extends React.Component {

    constructor(props) {

        super(props);
        this.state = {
            popup: false
        };
    }

    render() {
        return (<noscript></noscript>);
    }


    componentDidMount() {
        this.renderPopup();
    }

    componentDidUpdate() {
        this.renderPopup();
    }

    componentWillUnmount() {
        ReactDOM.unmountComponentAtNode(this.popup);
        document.body.removeChild(this.popup);
    }

    renderPopup() {
        if (!this.popup) {
            this.popup = document.createElement("div");
            document.body.appendChild(this.popup);
        }

        ReactDOM.render(
            <div className="warning-popup-overlay">
                <div className="warning-popup-content">
                    {this.props.children}
                </div>
            </div>,
            this.popup
        );
    }
}

export default WarningPopup;