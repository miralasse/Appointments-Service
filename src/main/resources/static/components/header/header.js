import React from 'react';

import './header.css';


class Header extends React.Component {

    render() {
        return (
            <h1 className="sectionHeader">{this.props.text}</h1>
        );
    }

}

export default Header;